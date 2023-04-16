package ru.spbstu.kafka.message;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.spbstu.antispam.SpamDocument;

import ru.spbstu.messages.MessagesService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class SpamDocumentEventProcessorTest {

    private static final String TOPIC = "testTopic";

    @Mock
    private MessagesService messagesService;

    private SpamDocumentEventProcessor processor;

    @Before
    public void setUp() {
        Mockito.doNothing().when(messagesService).processMessage(Mockito.any());
        this.processor = new SpamDocumentEventProcessor(messagesService);
    }

    @Test(expected = NullPointerException.class)
    public void processNullInputTest() throws Exception {
        processor.process(null);
    }

    @Test
    public void processEmptyMapInputTest() throws Exception {
        processor.process(Collections.emptyMap());
        Mockito.verify(messagesService, Mockito.times(0)).processMessage(Mockito.any());
    }

    @Test
    public void processNotEmptyMapInputTest() throws Exception {
        SpamDocument firstSpamDocument = new SpamDocument(
                new SpamDocumentDTO("CHAT", "1", 1L, 2L, "text_1", System.currentTimeMillis() - 1000 * 60));
        SpamDocument secondSpamDocument = new SpamDocument(
                new SpamDocumentDTO("USER_MESSAGE", "2", 3L, 4L, "text_2", System.currentTimeMillis()));
        List<SpamDocument> spamDocuments = ImmutableList.of(firstSpamDocument, secondSpamDocument);
        Map<String, List<SpamDocument>> messages = ImmutableMap.of(TOPIC, spamDocuments);
        processor.process(messages);
        Mockito.verify(messagesService, Mockito.times(2)).processMessage(Mockito.any());
        Mockito.verify(messagesService, Mockito.times(1))
                .processMessage(Mockito.refEq(firstSpamDocument));
        Mockito.verify(messagesService, Mockito.times(1))
                .processMessage(Mockito.refEq(secondSpamDocument));
    }

}
