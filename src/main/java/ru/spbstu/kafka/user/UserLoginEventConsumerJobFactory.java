package ru.spbstu.kafka.user;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.spbstu.antispam.UserLogin;
import ru.spbstu.kafka.base.KafkaConsumerJobFactory;
import ru.spbstu.kafka.utils.PropertiesProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;

@Component
public class UserLoginEventConsumerJobFactory {

    private static final String componentId = "userLogins";
    private static final String CONSUMER_PROPERTIES_FILE_PATH = "./src/main/resources/userlogin-consumer-properties.properties";

    private final KafkaConsumerJobFactory<UserLogin> consumerJobFactory;

    @Autowired
    public UserLoginEventConsumerJobFactory(@NotNull UserLoginConfiguration userLoginConfiguration,
                                            @NotNull UserLoginEventParser userLoginEventParser,
                                            @NotNull UserLoginEventProcessor userLoginEventProcessor) {
        Validate.notNull(userLoginConfiguration);
        Validate.notNull(userLoginEventParser);
        Validate.notNull(userLoginEventProcessor);
        Properties consumerProperties = PropertiesProvider.provideProperties(CONSUMER_PROPERTIES_FILE_PATH);
        this.consumerJobFactory = new KafkaConsumerJobFactory<>(componentId, consumerProperties, userLoginConfiguration, userLoginEventProcessor, userLoginEventParser);
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
