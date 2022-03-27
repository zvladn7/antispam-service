package ru.spbstu.ip;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CorrelationBasedChekVerificationAlgorithm {

    private static final Logger logger = LoggerFactory.getLogger(CorrelationBasedChekVerificationAlgorithm.class);

    private double getCorrelation(long userId,
                                  @NotNull IpEntryList ipEntryList,
                                  @NotNull IpEntry newIpEntry) {

    }

}
