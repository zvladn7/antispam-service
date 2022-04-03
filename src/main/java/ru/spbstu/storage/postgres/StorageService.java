package ru.spbstu.storage.postgres;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbstu.antispam.Activity;
import ru.spbstu.antispam.ActivityInfo;
import ru.spbstu.ip.IpEntry;
import ru.spbstu.ip.IpEntryList;
import ru.spbstu.storage.ip.GeoIP;
import ru.spbstu.storage.postgres.dto.IpEntryDTO;
import ru.spbstu.storage.postgres.dto.IpEntryListDTO;
import ru.spbstu.storage.postgres.dto.IpInfo;
import ru.spbstu.storage.postgres.dto.UserInfo;
import ru.spbstu.storage.postgres.repo.IpEntryRepository;
import ru.spbstu.storage.postgres.repo.IpInfoRepository;
import ru.spbstu.storage.postgres.repo.UserInfoRepository;

import java.util.*;

@Service
public class StorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);

    private final IpInfoRepository ipInfoRepository;
    private final UserInfoRepository userInfoRepository;
    private final IpEntryRepository ipEntryRepository;

    @Autowired
    public StorageService(@NotNull IpInfoRepository ipInfoRepository,
                          @NotNull UserInfoRepository userInfoRepository,
                          @NotNull IpEntryRepository ipEntryRepository) {
        this.ipInfoRepository = ipInfoRepository;
        this.userInfoRepository = userInfoRepository;
        this.ipEntryRepository = ipEntryRepository;
    }

    @NotNull
    public Pair<IpEntryList, List<ActivityInfo>> getIpEntryList(long userId) {
        Optional<UserInfo> optionalUserInfo = userInfoRepository.findById(userId);
        if (optionalUserInfo.isEmpty()) {
            return Pair.of(null, null);
        }
        UserInfo userInfo = optionalUserInfo.get();
        IpEntryListDTO ipEntryListDTO = userInfo.getIpEntryListDTO();
        List<IpEntry> ipEntries = new ArrayList<>();
        for (IpEntryDTO dto : ipEntryListDTO.getIpEntryList()) {
            long id = dto.getId();
            long userIdDto = dto.getUserId();
            String ipAddress = dto.getIpAddress();
            String city = dto.getCity();
            double latitude = dto.getLatitude();
            double longitude = dto.getLongitude();
            int firstTime = dto.getFirstTime();
            int lastTime = dto.getLastTime();
            boolean verified = dto.isVerified();
            GeoIP geoIP = new GeoIP(ipAddress, city, latitude, longitude);
            ipEntries.add(new IpEntry(id, userIdDto, geoIP, firstTime, lastTime, verified));
        }
        return Pair.of(new IpEntryList(ipEntries), userInfo.getUserActivities());
    }

    public void saveIpEntry(@NotNull IpEntry ipEntry) {
        if (ipEntry.getId() != null) {
            updateIpEntry(ipEntry);
            return;
        }
        GeoIP geoIPInfo = ipEntry.getGeoIPInfo();
        IpEntryDTO dto = new IpEntryDTO(
                ipEntry.getUserId(),
                geoIPInfo.getIpAddress(),
                geoIPInfo.getCity(),
                geoIPInfo.getLatitude(),
                geoIPInfo.getLongitude(),
                ipEntry.getFirstTime(),
                ipEntry.getLastTime(),
                ipEntry.isVerified()
        );
        ipEntryRepository.save(dto);
    }

    public void updateIpEntry(@NotNull IpEntry ipEntry) {
        GeoIP geoIPInfo = ipEntry.getGeoIPInfo();
        IpEntryDTO dto = new IpEntryDTO(
                ipEntry.getId(),
                ipEntry.getUserId(),
                geoIPInfo.getIpAddress(),
                geoIPInfo.getCity(),
                geoIPInfo.getLatitude(),
                geoIPInfo.getLongitude(),
                ipEntry.getFirstTime(),
                ipEntry.getLastTime(),
                ipEntry.isVerified()
        );
        ipEntryRepository.save(dto);
    }

    public void saveUserInfo(@Nullable UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        try {
            userInfoRepository.save(userInfo);
        } catch (RuntimeException e) {
            log.warn("Unable to save user info to repository, userInfo=[{}]", userInfo, e);
        }
    }

    public void saveIpInfoRepository(@Nullable IpInfo ipInfo) {
        if (ipInfo == null) {
            return;
        }
        try {
            ipInfoRepository.save(ipInfo);
        } catch (RuntimeException e) {
            log.warn("Unable to save ip info to repository, userInfo=[{}]", ipInfo, e);
        }
    }

    @Nullable
    public UserInfo getUserInfo(@Nullable Long userId) {
        if (userId == null) {
            return null;
        }
        try {
            return userInfoRepository.findById(userId).orElse(null);
        } catch (RuntimeException e) {
            log.warn("Failed to get UserInfo by userId=[{}]", userId, e);
            return null;
        }
    }

    @NotNull
    public List<ActivityInfo> getUserActivities(@Nullable Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        try {
            return userInfoRepository.findById(userId)
                    .map(UserInfo::getUserActivities)
                    .orElse(Collections.emptyList());
        } catch (RuntimeException e) {
            log.warn("Unable to get all user activities from database, userId=[{}]", userId, e);
            return Collections.emptyList();
        }
    }

    @NotNull
    public List<Long> getIpUsers(@Nullable String ip,
                                      long userId) {
        if (ip == null) {
            return Collections.singletonList(userId);
        }
        try {
            List<UserInfo> allUsers = userInfoRepository.findAll();
            List<Long> ipUserIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(allUsers)) {
                for (UserInfo userInfo : allUsers) {
                    List<ActivityInfo> userActivities = userInfo.getUserActivities();
                    for (ActivityInfo activityInfo : userActivities) {
                        if (Activity.USER_LAST_IP_HASH.getName().equals(activityInfo.getActivity().getName())) {
                            int ipHash = activityInfo.getValue();
                            if (ipHash == ip.hashCode() && userId != userInfo.getUserId()) {
                                ipUserIds.add(userInfo.getUserId());
                            }
                        }
                    }
                }
            }
            ipUserIds.add(userId);
            return ipUserIds;
        } catch (Exception ex) {
            log.error("Cannot get all users for ip [{}] when processing [{}]", ip, userId, ex);
            return Collections.singletonList(userId);
        }
    }

    @NotNull
    public List<ActivityInfo> getIpActivities(@Nullable String ip) {
        if (ip == null) {
            return Collections.emptyList();
        }
        try {
            return ipInfoRepository.findById(ip)
                    .map(IpInfo::getIpActivities)
                    .orElse(Collections.emptyList());
        } catch (RuntimeException e) {
            log.warn("Unable to get all ip activities from database, ip=[{}]", ip, e);
            return Collections.emptyList();
        }
    }

}
