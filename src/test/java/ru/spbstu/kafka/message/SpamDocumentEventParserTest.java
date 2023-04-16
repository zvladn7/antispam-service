package ru.spbstu.kafka.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbstu.antispam.SpamDocument;

public class SpamDocumentEventParserTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String MESSAGE_TYPE = "CHAT";
    private static final String MESSAGE_ID = "1";
    private static final Long SENDER_ID = 1L;
    private static final Long RECEIVER_ID = 2L;
    private static final String TEXT = "test_text";
    private static final Long CREATED = System.currentTimeMillis();

    private SpamDocumentEventParser parser;

    @Before
    public void setUp() {
        this.parser = new SpamDocumentEventParser(OBJECT_MAPPER);
    }

    @Test(expected = NullPointerException.class)
    public void parseNullInputTest() {
        parser.parse(null);
    }

    @Test(expected = RuntimeException.class)
    public void parseNotParseableInputTest() {
        parser.parse("fklanlfjaskf");
    }

    @Test
    public void parseParseableInputTest() {
        String message = "{\"message_type\" : \"" + MESSAGE_TYPE + "\"," +
                "\"message_id\" : \"" + MESSAGE_ID + "\", " +
                "\"sender_id\" : " + SENDER_ID + ", " +
                "\"receiver_id\" : " + RECEIVER_ID + ", " +
                "\"text\" : \"" + TEXT + "\", " +
                "\"created\" : " + CREATED + " }";
        SpamDocument spamDocument = parser.parse(message);
        Assert.assertNotNull(spamDocument);
        Assert.assertEquals(MESSAGE_TYPE, spamDocument.getMessageType());
        Assert.assertEquals(MESSAGE_ID, spamDocument.getMessageId());
        Assert.assertEquals(SENDER_ID, spamDocument.getSenderId());
        Assert.assertEquals(RECEIVER_ID, spamDocument.getReceiverId());
        Assert.assertEquals(TEXT, spamDocument.getText());
    }

}
