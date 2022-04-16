package ru.spbstu.kafka.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.spbstu.antispam.SpamDocument;
import ru.spbstu.kafka.base.KafkaConsumerJob;
import ru.spbstu.kafka.base.KafkaConsumerJobFactory;
import ru.spbstu.kafka.utils.PropertiesProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;

@Component
public class SpamDocumentEventConsumerJobFactory {

    private static final String componentId = "spamDocument";
    private static final String CONSUMER_PROPERTIES_FILE_PATH = "./src/main/resources/messages-consumer-properties.properties";

    private final KafkaConsumerJobFactory<SpamDocument> consumerJobFactory;

    @Autowired
    public SpamDocumentEventConsumerJobFactory(@NotNull SpamDocumentConfiguration spamDocumentConfiguration,
                                               @NotNull SpamDocumentEventParser spamDocumentEventParse,
                                               @NotNull SpamDocumentEventProcessor spamDocumentEventProcessor) {
        Validate.notNull(spamDocumentConfiguration);
        Validate.notNull(spamDocumentEventParse);
        Validate.notNull(spamDocumentEventProcessor);
        Properties consumerProperties = PropertiesProvider.provideProperties(CONSUMER_PROPERTIES_FILE_PATH);
        this.consumerJobFactory = new KafkaConsumerJobFactory<>(componentId, consumerProperties, spamDocumentConfiguration, spamDocumentEventProcessor, spamDocumentEventParse);
    }

    @PostConstruct
    public void init() {
        consumerJobFactory.init();
    }

    @PreDestroy
    public void shutdown() {
        consumerJobFactory.shutdown();
    }

}
