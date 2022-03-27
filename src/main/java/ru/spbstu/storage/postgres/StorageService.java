package ru.spbstu.storage.postgres;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Nullable
    public IpEntryList getIpEntryList(long userId) {
        Optional<UserInfo> optionalUserInfo = userInfoRepository.findById(userId);
        if (!optionalUserInfo.isPresent()) {
            return null;
        }
        UserInfo userInfo = optionalUserInfo.get();
        IpEntryListDTO ipEntryListDTO = userInfo.getIpEntryListDTO();
        List<IpEntry> ipEntries = new ArrayList<>();
        for (IpEntryDTO dto : ipEntryListDTO.getIpEntryList()) {
            long userIdDto = dto.getUserId();
            String ipAddress = dto.getIpAddress();
            String city = dto.getCity();
            double latitude = dto.getLatitude();
            double longitude = dto.getLongitude();
            int firstTime = dto.getFirstTime();
            int lastTime = dto.getLastTime();
            boolean verified = dto.isVerified();
            GeoIP geoIP = new GeoIP(ipAddress, city, latitude, longitude);
            ipEntries.add(new IpEntry(userIdDto, geoIP, firstTime, lastTime, verified));
        }
        return new IpEntryList(ipEntries);
    }

    public void saveIpEntry(@NotNull IpEntry ipEntry) {
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

    @NotNull List<ActivityInfo> getIpActivities(@Nullable Long ipId) {
        if (ipId == null) {
            return Collections.emptyList();
        }
        try {
            return ipInfoRepository.findById(ipId)
                    .map(IpInfo::getIpActivities)
                    .orElse(Collections.emptyList());
        } catch (RuntimeException e) {
            log.warn("Unable to get all ip activities from database, ipId=[{}]", ipId, e);
            return Collections.emptyList();
        }
    }

}
