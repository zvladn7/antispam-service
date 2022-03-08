package ru.spbstu.kafka.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpamDocumentDTO {

    private static final String MESSAGE_TYPE = "message_type";
    private static final String MESSAGE_ID = "message_id";
    private static final String SENDER_ID = "sender_id";
    private static final String RECEIVER_ID = "receiver_id";
    private static final String TEXT = "text";
    private static final String CREATED = "created";

    private final String messageType;
    private final String messageId;
    private final Long senderId;
    private final Long receiverId;
    private final String text;
    private final long created;

    public SpamDocumentDTO(@JsonProperty(value = MESSAGE_TYPE, required = true) String messageType,
                           @JsonProperty(value = MESSAGE_ID, required = true) String messageId,
                           @JsonProperty(value = SENDER_ID, required = true) long senderId,
                           @JsonProperty(value = RECEIVER_ID) Long receiverId,
                           @JsonProperty(value = TEXT, required = true) String text,
                           @JsonProperty(value = CREATED, required = true) long created) {
        this.messageType = messageType;
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.created = created;
    }

    @JsonProperty(value = MESSAGE_TYPE)
    public String getMessageType() {
        return messageType;
    }

    @JsonProperty(value = MESSAGE_ID)
    public String getMessageId() {
        return messageId;
    }

    @JsonProperty(value = SENDER_ID)
    public Long getSenderId() {
        return senderId;
    }

    @JsonProperty(value = RECEIVER_ID)
    public Long getReceiverId() {
        return receiverId;
    }

    @JsonProperty(value = TEXT)
    public String getText() {
        return text;
    }

    @JsonProperty(value = CREATED)
    public long getCreated() {
        return created;
    }

}
