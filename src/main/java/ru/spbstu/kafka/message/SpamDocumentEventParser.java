package ru.spbstu.kafka.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.spbstu.antispam.SpamDocument;
import ru.spbstu.kafka.base.MessageParser;

@Component
public class SpamDocumentEventParser implements MessageParser<SpamDocument> {

    private static final Logger log = LoggerFactory.getLogger(SpamDocumentEventParser.class);

    private final ObjectMapper mapper;

    @Autowired
    public SpamDocumentEventParser(@NotNull ObjectMapper mapper) {
        Validate.notNull(mapper);
        this.mapper = mapper;
    }

    @NotNull
    @Override
    public SpamDocument parse(@NotNull String kafkaMessage) {
        try {
            SpamDocumentDTO spamDocumentDTO = mapper.readValue(kafkaMessage, SpamDocumentDTO.class);
            return new SpamDocument(spamDocumentDTO);
        } catch (Exception e) {
            log.error("Cannot parse SpamDocumentDTO from kafka message \n [{}]", kafkaMessage, e);
            throw new RuntimeException(e);
        }
    }

}
