package ru.spbstu.kafka.message;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.spbstu.antispam.SpamDocument;
import ru.spbstu.kafka.base.MessageProcessor;

import java.util.List;
import java.util.Map;

@Component
public class SpamDocumentEventProcessor implements MessageProcessor<SpamDocument> {

    private static final Logger log = LoggerFactory.getLogger(SpamDocumentEventProcessor.class);

    private SpamDocumentConfiguration spamDocumentConfiguration;

    public SpamDocumentEventProcessor(@NotNull SpamDocumentConfiguration spamDocumentConfiguration) {
        Validate.notNull(spamDocumentConfiguration);
        this.spamDocumentConfiguration = spamDocumentConfiguration;
    }

    @Override
    public void process(@NotNull Map<String, List<SpamDocument>> messages) throws Exception {
        messages.forEach((topic, documents) -> {
            documents.forEach(document -> processMessage(topic, document));
        });
    }

    private void processMessage(@NotNull String topic,
                                @NotNull SpamDocument document) {
        log.info(document.toString());
    }
}
