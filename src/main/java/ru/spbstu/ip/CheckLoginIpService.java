package ru.spbstu.ip;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.spbstu.antispam.UserLogin;
import ru.spbstu.storage.ip.GeoIP;
import ru.spbstu.storage.ip.GeoIpDAO;
import ru.spbstu.storage.postgres.StorageService;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class CheckLoginIpService{

    private static final Logger logger = LoggerFactory.getLogger(CheckLoginIpService.class);
    private static final int QUEUE_SIZE = 10000;

    private final StorageService storageService;
    private final GeoIpDAO geoIpDAO;
    private final CorrelationBasedChekVerificationAlgorithm verificationAlgorithm;
    private ExecutorService executorService;

    public CheckLoginIpService(@NotNull StorageService storageService,
                               @NotNull GeoIpDAO geoIpDAO,
                               @NotNull CorrelationBasedChekVerificationAlgorithm verificationAlgorithm) {
        Validate.notNull(storageService);
        Validate.notNull(geoIpDAO);
        Validate.notNull(verificationAlgorithm);

        this.storageService = storageService;
        this.geoIpDAO = geoIpDAO;
        this.verificationAlgorithm = verificationAlgorithm;
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
        executorService.submit(() -> {
            String loginIpAddress = userLogin.getLoginIpAddress();
            long userId = userLogin.getUserId();
            IpEntryList ipEntryList = storageService.getIpEntryList(userId);
            GeoIP geoIP = geoIpDAO.getLocation(loginIpAddress);
            if (geoIP == null) {
                logger.warn("failed to process UserLogin: [{}] cause geoIp is null", userLogin);
                return;
            }
            IpEntry ipEntry = new IpEntry(userId, geoIP);
            CheckIpResult checkIpResult = verificationAlgorithm.checkIP(ipEntryList, ipEntry);

        });
    }

}
