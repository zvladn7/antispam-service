package ru.spbstu.ip;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.spbstu.antispam.Activity;
import ru.spbstu.antispam.ActivityInfo;
import ru.spbstu.antispam.UserLogin;
import ru.spbstu.kafka.publisher.ActivityInfoDTO;
import ru.spbstu.kafka.publisher.ActivityInfoMapper;
import ru.spbstu.kafka.publisher.ErrorCode;
import ru.spbstu.kafka.publisher.IpResult;
import ru.spbstu.kafka.publisher.KafkaPublisher;
import ru.spbstu.storage.ip.GeoIP;
import ru.spbstu.storage.ip.GeoIpDAO;
import ru.spbstu.storage.postgres.StorageService;
import ru.spbstu.storage.postgres.dto.IpInfo;
import ru.spbstu.storage.postgres.dto.UserInfo;
import ru.spbstu.util.DateUtil;
import ru.spbstu.vpn.VPNDetection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckLoginIpService {

    private static final Logger logger = LoggerFactory.getLogger(CheckLoginIpService.class);

    private final StorageService storageService;
    private final GeoIpDAO geoIpDAO;
    private final CorrelationBasedChekVerificationAlgorithm verificationAlgorithm;
    private final KafkaPublisher<IpResult> ipResultKafkaPublisher;

    public CheckLoginIpService(@NotNull StorageService storageService,
                               @NotNull GeoIpDAO geoIpDAO,
                               @NotNull CorrelationBasedChekVerificationAlgorithm verificationAlgorithm,
                               @NotNull KafkaPublisher<IpResult> ipResultKafkaPublisher) {
        Validate.notNull(storageService);
        Validate.notNull(geoIpDAO);
        Validate.notNull(verificationAlgorithm);
        Validate.notNull(ipResultKafkaPublisher);

        this.storageService = storageService;
        this.geoIpDAO = geoIpDAO;
        this.verificationAlgorithm = verificationAlgorithm;
        this.ipResultKafkaPublisher = ipResultKafkaPublisher;
    }

    public void processLogin(@NotNull UserLogin userLogin) {
        long userId = userLogin.getUserId();
        Pair<IpEntryList, List<ActivityInfo>> ipEntryActivitiesPair = storageService.getIpEntryList(userId);
        List<ActivityInfoDTO> userActivityInfos = processLoginUser(userLogin, ipEntryActivitiesPair.getValue());
        Triple<Double, List<ActivityInfoDTO>, ErrorCode> processLoginIpResult
                = processLoginIp(userLogin, ipEntryActivitiesPair.getKey());
        boolean ipHostVpn = isIpHostVpn(userLogin.getLoginIpAddress());
        ipResultKafkaPublisher.publish(new IpResult(
                userId,
                userLogin.getLoginIpAddress(),
                ipHostVpn,
                processLoginIpResult.getRight(),
                processLoginIpResult.getLeft(),
                processLoginIpResult.getMiddle(),
                userActivityInfos
        ));
    }

    private boolean isIpHostVpn(@NotNull String ipToLookup) {
        try {
            return new VPNDetection().getResponse(ipToLookup).hostip;
        } catch (IOException e) {
            logger.warn("Failed to detect vpn for ip: [{}]", ipToLookup, e);
            return false;
        }
    }

    private List<ActivityInfoDTO> processLoginUser(@NotNull UserLogin userLogin,
                                                   @Nullable List<ActivityInfo> userActivityInfos) {
        List<ActivityInfo> updatedActivityInfos = updateUserActivities(userLogin, userActivityInfos);
        storageService.saveUserInfo(new UserInfo(
                userLogin.getUserId(),
                updatedActivityInfos
        ));
        return ActivityInfoMapper.convert(updatedActivityInfos);
    }

    private Triple<Double, List<ActivityInfoDTO>, ErrorCode> processLoginIp(@NotNull UserLogin userLogin,
                                                                            @Nullable IpEntryList ipEntryList) {
        List<Long> userIds = storageService.getIpUsers(userLogin.getLoginIpAddress(), userLogin.getUserId());
        List<ActivityInfo> ipActivities = updateIpActivities(userIds.size(), userLogin.getLoginIpAddress());
        List<ActivityInfoDTO> ipActivitiesDTOList = ActivityInfoMapper.convert(ipActivities);
        storageService.saveIpInfoRepository(new IpInfo(
                userLogin.getLoginIpAddress(),
                ipActivities,
                userIds
        ));
        GeoIP geoIP = geoIpDAO.getLocation(userLogin.getLoginIpAddress());
        if (geoIP == null) {
            logger.warn("failed to process UserLogin: [{}] cause geoIp is null", userLogin);
            return Triple.of(null, ipActivitiesDTOList, ErrorCode.CANNOT_CHECK_GEO_IP);
        }
        IpEntry ipEntry = new IpEntry(userLogin.getUserId(), geoIP);
        CheckIpResult checkIpResult = verificationAlgorithm.checkIP(ipEntryList, ipEntry);
        storageService.saveIpEntry(ipEntry);
        return Triple.of(checkIpResult.getCorrelation(), ipActivitiesDTOList, null);
    }

    private List<ActivityInfo> updateIpActivities(int amountOfUsers,
                                                  @Nullable String ip) {
        List<ActivityInfo> ipActivities = storageService.getIpActivities(ip);
        List<ActivityInfo> newIpActivityInfos = new ArrayList<>();
        addActivityIfFirstTime(ipActivities, newIpActivityInfos, Activity.IP_FIRST_TIME);
        newIpActivityInfos.add(new ActivityInfo(Activity.IP_LAST_TIME, DateUtil.currentDateCompact()));
        newIpActivityInfos.add(new ActivityInfo(Activity.IP_USERS_COUNT, amountOfUsers));
        return newIpActivityInfos;
    }

    private List<ActivityInfo> updateUserActivities(@NotNull UserLogin userLogin,
                                                    @Nullable List<ActivityInfo> userActivityInfos) {
        List<ActivityInfo> newUserActivitiesInfo = new ArrayList<>();
        addActivityIfFirstTime(userActivityInfos, newUserActivitiesInfo, Activity.USER_FIRST_TIME_LOGIN);
        newUserActivitiesInfo.add(new ActivityInfo(Activity.USER_LAST_TIME_LOGIN, DateUtil.currentDateCompact()));
        newUserActivitiesInfo.add(new ActivityInfo(Activity.USER_LAST_IP_HASH, userLogin.getLoginIpAddress().length()));
        return newUserActivitiesInfo;
    }

    private void addActivityIfFirstTime(@Nullable List<ActivityInfo> activityInfos,
                                        @NotNull List<ActivityInfo> newActivityInfos,
                                        @NotNull Activity activity) {
        boolean firstTimeLogin = true;
        if (!CollectionUtils.isEmpty(activityInfos)) {
            for (ActivityInfo activityInfo : activityInfos) {
                if (activity.getName().equals(activityInfo.getActivity().getName())) {
                    firstTimeLogin = false;
                    break;
                }
            }
        }
        if (firstTimeLogin) {
            newActivityInfos.add(new ActivityInfo(activity, DateUtil.currentDateCompact()));
        }
    }

}
