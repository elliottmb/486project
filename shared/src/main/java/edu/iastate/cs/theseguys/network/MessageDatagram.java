package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.database.MessageRecord;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

public class MessageDatagram implements Serializable {
    private static final long serialVersionUID = -812254474476469252L;
    private final UUID id;
    private final UUID userId;
    private final UUID fatherId;
    private final UUID motherId;
    private final String messageBody;
    private final Timestamp timestamp;
    private final byte[] signature;

    public MessageDatagram(UUID id, UUID userId, UUID fatherId, UUID motherId, String messageBody, Timestamp timestamp, byte[] signature) {
        this.id = id;
        this.userId = userId;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.messageBody = messageBody;
        this.timestamp = timestamp;
        this.signature = signature;
    }

    public MessageDatagram(MessageRecord messageRecord) {
        this.id = messageRecord.getId();
        this.userId = messageRecord.getUserId();
        this.fatherId = messageRecord.getFather().getId();
        this.motherId = messageRecord.getMother().getId();
        this.messageBody = messageRecord.getMessageBody();
        this.timestamp = messageRecord.getTimestamp();
        this.signature = messageRecord.getSignature();
    }

    @Override
    public String toString() {
        return "MessageDatagram{" +
                "id=" + id +
                ", userId=" + userId +
                ", fatherId=" + fatherId +
                ", motherId=" + motherId +
                ", messageBody='" + messageBody + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getFatherId() {
        return fatherId;
    }

    public UUID getMotherId() {
        return motherId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public byte[] getSignature() {
        return signature;
    }
}
