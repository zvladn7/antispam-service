// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.spbstu.antispam.UserLogin;
import ru.spbstu.kafka.base.MessageParser;

@Component
public class UserLoginEventParser implements MessageParser<UserLogin> {

    private final ObjectMapper mapper;

    @Autowired
    public UserLoginEventParser(@NotNull ObjectMapper mapper) {
        Validate.notNull(mapper);
        this.mapper = mapper;
    }

    @NotNull
    @Override
    public UserLogin parse(@NotNull String kafkaMessage) {
        Validate.notNull(kafkaMessage);
        try {
            UserLoginDTO userLoginDTO = mapper.readValue(kafkaMessage, UserLoginDTO.class);
            return new UserLogin(userLoginDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to parse user login message: " + kafkaMessage, e);
        }
    }
}
