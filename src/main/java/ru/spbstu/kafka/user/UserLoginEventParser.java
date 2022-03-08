package ru.spbstu.kafka.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.spbstu.antispam.UserLogin;
import ru.spbstu.kafka.base.MessageParser;

@Component
public class UserLoginEventParser implements MessageParser<UserLogin> {

    private static final Logger log = LoggerFactory.getLogger(UserLoginEventParser.class);

    private final ObjectMapper mapper;

    @Autowired
    public UserLoginEventParser(@NotNull ObjectMapper mapper) {
        Validate.notNull(mapper);
        this.mapper = mapper;
    }

    @NotNull
    @Override
    public UserLogin parse(@NotNull String kafkaMessage) {
        try {
            UserLoginDTO userLoginDTO = mapper.readValue(kafkaMessage, UserLoginDTO.class);
            return new UserLogin(userLoginDTO);
        } catch (Exception e) {
            log.error("Cannot parse UserLoginDTO from kafka message", e);
            throw new RuntimeException(e);
        }
    }
}
