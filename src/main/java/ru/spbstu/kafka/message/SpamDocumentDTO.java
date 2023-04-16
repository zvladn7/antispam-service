// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpamDocumentDTO {

    private static final String JSON_MESSAGE_TYPE = "message_type";
    private static final String JSON_MESSAGE_ID = "message_id";
    private static final String JSON_SENDER_ID = "sender_id";
    private static final String JSON_RECEIVER_ID = "receiver_id";
    private static final String JSON_TEXT = "text";
    private static final String JSON_CREATED = "created";

    private final String messageType;
    private final String messageId;
    private final Long senderId;
    private final Long receiverId;
    private final String text;
    private final long created;

    public SpamDocumentDTO(@JsonProperty(value = JSON_MESSAGE_TYPE, required = true) String messageType,
                           @JsonProperty(value = JSON_MESSAGE_ID, required = true) String messageId,
                           @JsonProperty(value = JSON_SENDER_ID, required = true) long senderId,
                           @JsonProperty(value = JSON_RECEIVER_ID) Long receiverId,
                           @JsonProperty(value = JSON_TEXT, required = true) String text,
                           @JsonProperty(value = JSON_CREATED, required = true) long created) {
        this.messageType = messageType;
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.created = created;
    }

    @JsonProperty(value = JSON_MESSAGE_TYPE)
    public String getMessageType() {
        return messageType;
    }

    @JsonProperty(value = JSON_MESSAGE_ID)
    public String getMessageId() {
        return messageId;
    }

    @JsonProperty(value = JSON_SENDER_ID)
    public Long getSenderId() {
        return senderId;
    }

    @JsonProperty(value = JSON_RECEIVER_ID)
    public Long getReceiverId() {
        return receiverId;
    }

    @JsonProperty(value = JSON_TEXT)
    public String getText() {
        return text;
    }

    @JsonProperty(value = JSON_CREATED)
    public long getCreated() {
        return created;
    }

}
