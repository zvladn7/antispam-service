// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.user;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.spbstu.antispam.UserLogin;
import ru.spbstu.ip.CheckLoginIpService;
import ru.spbstu.kafka.base.MessageProcessor;

import java.util.List;
import java.util.Map;

@Component
public class UserLoginEventProcessor implements MessageProcessor<UserLogin> {

    private static final Logger log = LoggerFactory.getLogger(UserLoginEventProcessor.class);

    private final CheckLoginIpService checkLoginIpService;

    public UserLoginEventProcessor(@NotNull CheckLoginIpService checkLoginIpService) {
        Validate.notNull(checkLoginIpService);

        this.checkLoginIpService = checkLoginIpService;
    }

    @Override
    public void process(@NotNull Map<String, List<UserLogin>> messages) {
        messages.forEach((topic, userLoginsList) -> userLoginsList.forEach(userLogin -> processMessage(topic, userLogin)));
    }

    private void processMessage(@NotNull String topic,
                                @NotNull UserLogin userLogin) {
        checkLoginIpService.processLogin(userLogin);
        log.debug("UserLogin: [{}] message is processed from topic: [{}]", userLogin, topic);
    }
}
