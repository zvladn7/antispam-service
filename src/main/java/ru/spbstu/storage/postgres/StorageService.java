package ru.spbstu.storage.postgres;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbstu.antispam.ActivityInfo;
import ru.spbstu.storage.postgres.dto.IpInfo;
import ru.spbstu.storage.postgres.dto.UserInfo;
import ru.spbstu.storage.postgres.repo.IpInfoRepository;
import ru.spbstu.storage.postgres.repo.UserInfoRepository;

import java.util.Collections;
import java.util.List;

@Service
public class StorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);

    private final IpInfoRepository ipInfoRepository;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public StorageService(@NotNull IpInfoRepository ipInfoRepository,
                          @NotNull UserInfoRepository userInfoRepository) {
        this.ipInfoRepository = ipInfoRepository;
        this.userInfoRepository = userInfoRepository;
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
    public List<String> findAllUserIps(@Nullable Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        try {
            return userInfoRepository.findById(userId)
                    .map(UserInfo::getIps)
                    .orElse(Collections.emptyList());
        } catch (RuntimeException e) {
            log.warn("Unable to get all user ips from database, userId=[{}]", userId, e);
            return Collections.emptyList();
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
