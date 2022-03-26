package ru.spbstu.ip;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.spbstu.antispam.UserLogin;
import ru.spbstu.storage.postgres.dto.UserInfo;
import ru.spbstu.storage.postgres.repo.UserInfoRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Service
public class CheckLoginIpService{

    private static final Logger logger = LoggerFactory.getLogger(CheckLoginIpService.class);
    private static final int QUEUE_SIZE = 10000;

    private final UserInfoRepository userInfoRepository;
    private ExecutorService executorService;

    public CheckLoginIpService(@NotNull UserInfoRepository userInfoRepository) {
        Validate.notNull(userInfoRepository);
        this.userInfoRepository = userInfoRepository;
    }

    @PostConstruct
    public void init() {
        int workersNumber = Runtime.getRuntime().availableProcessors();
        executorService =  new ThreadPoolExecutor(workersNumber, workersNumber,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(QUEUE_SIZE),
                new ThreadFactoryBuilder()
                        .setNameFormat("worker-%d")
                        .setUncaughtExceptionHandler((t, e) -> logger.error("Error when processing request in: {}", t, e)
                        ).build(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    public void checkIpInAsyncPool(@NotNull UserLogin userLogin) {
        String loginIpAddress = userLogin.getLoginIpAddress();
        long userId = userLogin.getUserId();
        UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
        List<String> lastUserIps = userInfo == null ? Collections.emptyList() : userInfo.getIps();
        List<String> updatedUserIps = new ArrayList<>();
    }

    private List<String> getLastUserIps(long userId) {
        return userInfoRepository.findById(userId)
                .map(UserInfo::getIps)
                .orElse(Collections.emptyList());
    }
}
