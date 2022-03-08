package ru.spbstu.kafka.user;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.spbstu.antispam.SpamDocument;
import ru.spbstu.antispam.UserLogin;
import ru.spbstu.kafka.base.MessageProcessor;

import java.util.List;
import java.util.Map;

@Component
public class UserLoginEventProcessor implements MessageProcessor<UserLogin> {

    private static final Logger log = LoggerFactory.getLogger(UserLoginEventProcessor.class);

    private UserLoginConfiguration userLoginConfiguration;

    public UserLoginEventProcessor(@NotNull UserLoginConfiguration userLoginConfiguration) {
        Validate.notNull(userLoginConfiguration);
        this.userLoginConfiguration = userLoginConfiguration;
    }

    @Override
    public void process(@NotNull Map<String, List<UserLogin>> messages) {
        messages.forEach((topic, userLoginsList) -> userLoginsList.forEach(userLogin -> processMessage(topic, userLogin)));
    }

    private void processMessage(@NotNull String topic,
                                @NotNull UserLogin userLogin) {
        log.info(userLogin.toString());
    }
}
