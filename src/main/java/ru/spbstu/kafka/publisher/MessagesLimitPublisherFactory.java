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
public class MessagesLimitPublisherFactory {

    private static final String TOPIC = "messagesResultTopic";
    private static final String PUBLISHER_PROPERTIES_FILE_PATH = "./src/main/resources/messages-publisher.properties";

    private final ObjectMapper mapper;
    private final Properties publisherConfiguration;
    private KafkaPublisher<MessageResult> messageResultKafkaPublisher;

    public MessagesLimitPublisherFactory(@NotNull ObjectMapper mapper) {
        Validate.notNull(mapper);
        this.mapper = mapper;
        this.publisherConfiguration = PropertiesProvider.provideProperties(PUBLISHER_PROPERTIES_FILE_PATH);
    }

    @PostConstruct
    public void init() {
        messageResultKafkaPublisher = new KafkaPublisher<>(
                mapper,
                TOPIC,
                publisherConfiguration,
                messageResult -> String.valueOf(messageResult.getUserId()).hashCode()
        );
        messageResultKafkaPublisher.refresh();
    }

    @Bean(destroyMethod = "shutdown")
    public KafkaPublisher<MessageResult> messageResultKafkaPublisher() {
        return messageResultKafkaPublisher;
    }

}
