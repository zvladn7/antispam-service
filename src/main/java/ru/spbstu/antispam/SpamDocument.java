package ru.spbstu.antispam;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import ru.spbstu.kafka.message.SpamDocumentDTO;

public class SpamDocument {

    private final String messageType;
    private final String messageId;
    private final Long senderId;
    private final Long receiverId;
    private final String text;

    public SpamDocument(@NotNull SpamDocumentDTO documentDTO) {
        Validate.notNull(documentDTO);
        this.messageType = documentDTO.getMessageType();
        this.messageId = documentDTO.getMessageId();
        this.senderId = documentDTO.getSenderId();
        this.receiverId = documentDTO.getReceiverId();
        this.text = documentDTO.getText();
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessageId() {
        return messageId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "SpamDocument{" +
                "messageType='" + messageType + '\'' +
                ", messageId='" + messageId + '\'' +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", text='" + text + '\'' +
                '}';
    }

}
