// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.publisher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageResult {

    private static final String USER_ID_FIELD = "user_id";
    private static final String MESSAGE_TYPE_FIELD = "message_type";
    private static final String LIMIT_AMOUNT_FIELD = "limit_amount";

    private final long userId;
    private final String messageType;
    private final long limitAmount;

    public MessageResult(@JsonProperty(value = USER_ID_FIELD) long userId,
                         @JsonProperty(value = MESSAGE_TYPE_FIELD) String messageType,
                         @JsonProperty(value = LIMIT_AMOUNT_FIELD) long limitAmount) {
        this.userId = userId;
        this.messageType = messageType;
        this.limitAmount = limitAmount;
    }

    @JsonProperty(value = USER_ID_FIELD)
    public long getUserId() {
        return userId;
    }

    @JsonProperty(value = MESSAGE_TYPE_FIELD)
    public String getMessageType() {
        return messageType;
    }

    @JsonProperty(value = LIMIT_AMOUNT_FIELD)
    public long getLimitAmount() {
        return limitAmount;
    }

}
