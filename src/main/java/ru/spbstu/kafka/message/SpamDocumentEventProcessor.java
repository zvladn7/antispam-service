package ru.spbstu.kafka.message;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.spbstu.antispam.SpamDocument;
import ru.spbstu.kafka.base.MessageProcessor;
import ru.spbstu.messages.MessagesService;

import java.util.List;
import java.util.Map;

@Component
public class SpamDocumentEventProcessor implements MessageProcessor<SpamDocument> {

    private static final Logger logger = LoggerFactory.getLogger(SpamDocumentEventProcessor.class);

    private final MessagesService messagesService;

    @Autowired
    public SpamDocumentEventProcessor(@NotNull MessagesService messagesService) {
        Validate.notNull(messagesService);
        this.messagesService = messagesService;
    }

    @Override
    public void process(@NotNull Map<String, List<SpamDocument>> messages) throws Exception {
        messages.forEach((topic, documents) -> documents.forEach(document -> processMessage(topic, document)));
    }

    private void processMessage(@NotNull String topic,
                                @NotNull SpamDocument document) {
        try {
            messagesService.processMessage(document);
        } catch (Exception ex) {
            logger.warn("Failed to process document=[{}] from topic=[{}]", document, topic, ex);
        }
    }
}
