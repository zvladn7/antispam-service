// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.spbstu.kafka.utils.PropertiesProvider;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Component
public class IpResultPublisherFactory {

    private static final String TOPIC = "ipResultTopic";
    private static final String PUBLISHER_PROPERTIES_FILE_PATH = "./src/main/resources/ip-result-publisher.properties";

    private final ObjectMapper mapper;
    private final Properties publisherConfiguration;
    private KafkaPublisher<IpResult> ipResultKafkaPublisher;

    public IpResultPublisherFactory(@NotNull ObjectMapper mapper) {
        Validate.notNull(mapper);
        this.mapper = mapper;
        this.publisherConfiguration = PropertiesProvider.provideProperties(PUBLISHER_PROPERTIES_FILE_PATH);
    }

    @PostConstruct
    public void init() {
        ipResultKafkaPublisher = new KafkaPublisher<>(
                mapper,
                TOPIC,
                publisherConfiguration,
                ipResult -> String.valueOf(ipResult.getUserId()).hashCode()
        );
        ipResultKafkaPublisher.refresh();
    }

    @Bean(destroyMethod = "shutdown")
    public KafkaPublisher<IpResult> ipResultKafkaPublisher() {
        return ipResultKafkaPublisher;
    }

}
