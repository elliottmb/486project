package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.database.MessageRecord;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

public class MessageDatagram implements Serializable, Comparable<MessageDatagram> {
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

    /**
     * Copy a MessageDatagram's data, but apply a new signature
     *
     * @param messageDatagram
     * @param newSignature
     */
    public MessageDatagram(MessageDatagram messageDatagram, byte[] newSignature) {
        this.id = messageDatagram.getId();
        this.userId = messageDatagram.getUserId();
        this.fatherId = messageDatagram.getFatherId();
        this.motherId = messageDatagram.getMotherId();
        this.messageBody = messageDatagram.getMessageBody();
        this.timestamp = messageDatagram.getTimestamp();
        this.signature = newSignature;
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

    public String toSignable() {
        return "{id=" + id +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDatagram that = (MessageDatagram) o;

        if (!id.equals(that.id)) return false;
        if (!userId.equals(that.userId)) return false;
        if (!fatherId.equals(that.fatherId)) return false;
        if (!motherId.equals(that.motherId)) return false;
        if (!messageBody.equals(that.messageBody)) return false;
        if (!timestamp.equals(that.timestamp)) return false;
        return Arrays.equals(signature, that.signature);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + fatherId.hashCode();
        result = 31 * result + motherId.hashCode();
        result = 31 * result + messageBody.hashCode();
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + Arrays.hashCode(signature);
        return result;
    }

    @Override
    public int compareTo(MessageDatagram o) {
        return timestamp.compareTo(o.getTimestamp());
    }
}
