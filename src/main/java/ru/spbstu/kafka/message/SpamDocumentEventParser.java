// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.spbstu.antispam.SpamDocument;
import ru.spbstu.kafka.base.MessageParser;

@Component
public class SpamDocumentEventParser implements MessageParser<SpamDocument> {

    private final ObjectMapper mapper;

    @Autowired
    public SpamDocumentEventParser(@NotNull ObjectMapper mapper) {
        Validate.notNull(mapper);
        this.mapper = mapper;
    }

    @NotNull
    @Override
    public SpamDocument parse(@NotNull String kafkaMessage) {
        Validate.notNull(kafkaMessage);
        try {
            SpamDocumentDTO spamDocumentDTO = mapper.readValue(kafkaMessage, SpamDocumentDTO.class);
            return new SpamDocument(spamDocumentDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to parse spam document message: " + kafkaMessage, e);
        }
    }

}
