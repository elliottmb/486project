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

    /**
     * Creats a MessageDatagram with the given id, userId, fatherId, motherId, messageBody, timestamp, and signature
     * @param id
     * @param userId
     * @param fatherId
     * @param motherId
     * @param messageBody
     * @param timestamp
     * @param signature
     */
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

     /**
      * Creates a MessageDatagram out of the given MessageRecord messageRecord
      * @param messageRecord
      */
    public MessageDatagram(MessageRecord messageRecord) {
        this.id = messageRecord.getId();
        this.userId = messageRecord.getUserId();
        this.fatherId = messageRecord.getFather().getId();
        this.motherId = messageRecord.getMother().getId();
        this.messageBody = messageRecord.getMessageBody();
        this.timestamp = messageRecord.getTimestamp();
        this.signature = messageRecord.getSignature();
    }

    /**
     * Returns the id, userId, fatherId, motherId, messageBody, and timestamp as a MessageDatagram string
     */
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

    /**
     * Returns the id, userId, fatherId, motherId, messageBody, and timestamp as a signable string
     * @return
     */
    public String toSignable() {
        return "{id=" + id +
                ", userId=" + userId +
                ", fatherId=" + fatherId +
                ", motherId=" + motherId +
                ", messageBody='" + messageBody + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * gets the UUID id
     * @return id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the UUID userId
     * @return userId
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Gets the UUID fatherId
     * @return fatherId
     */
    public UUID getFatherId() {
        return fatherId;
    }

    /**
     * Gets the UUID motherId
     * @return motherId
     */
    public UUID getMotherId() {
        return motherId;
    }

    /**
     * Gets the String messageBody
     * @return messageBody
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * Gets the Timestamp timestamp
     * @return timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the byte[] signature
     * @return signature
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Overriden equals for our MessageDatagram. Compares id, userId, fatherId, motherId, messageBody, timestamp, and signature
     *
     *@return whether or not the MessageDatagrams are equal
     */
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

    /**
     * Overriden hashCode method. Hashcode is generated using the userId, fatherId, 
     * motherId, messageBody, timestamp ,and signature
     * 
     * @return the int hashcode result
     */
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

    /**
     * Overriden compareTo. We use the timestamp for our compareTo
     * 
     * @return the result of Timestamp.compareTo
     */
    @Override
    public int compareTo(MessageDatagram o) {
        return timestamp.compareTo(o.getTimestamp());
    }
}
