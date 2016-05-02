package edu.iastate.cs.theseguys.database;

import edu.iastate.cs.theseguys.network.MessageDatagram;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

/**
 * The persistent class for the message database table.
 */
@Entity
@Table(name = "message")
public class MessageRecord {
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "father_id")
    private Set<MessageRecord> leftChildren;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "mother_id")
    private Set<MessageRecord> rightChildren;
    @ManyToOne
    private MessageRecord father;
    @ManyToOne
    private MessageRecord mother;
    @Id
    @Column(columnDefinition = "UUID NOT NULL")
    private UUID id;
    @Column(columnDefinition = "UUID NOT NULL")
    private UUID userId;
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String messageBody;
    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private Timestamp timestamp;
    @Column(columnDefinition = "BLOB NOT NULL")
    private byte[] signature;

    /**
     * Creats a basic MessageRecord
     */
    public MessageRecord() {

    }

    /**
     * Creates a MessageRecord with the given id, userId, messageBody, timestamp, and signature
     * @param id
     * @param userId
     * @param messageBody
     * @param timestamp
     * @param signature
     */
    public MessageRecord(UUID id, UUID userId, String messageBody, Timestamp timestamp, byte[] signature) {
        this.id = id;
        this.userId = userId;
        this.messageBody = messageBody;
        this.timestamp = timestamp;
        this.signature = signature;
    }

    /**
     * Returns the id, userId, timestamp, messageBody, fatherId, and motherId as a MessageRecord string
     */
    @Override
    public String toString() {
        return String.format(
                "MessageRecord[id='%s', userId='%s', timestamp='%s', body='%s', fatherId='%s', motherId='%s']",
                id,
                userId,
                timestamp,
                messageBody,
                father.getId(),
                mother.getId()
        );
    }

    /**
     * Gets the UUID id
     * @return id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id with the UUID id
     * @param id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the UUID userId
     * @return
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Sets the userId with the UUID userId
     * @param userId
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * Gets the MessageRecord father
     * @return father
     */
    public MessageRecord getFather() {
        return father;
    }

    /**
     * Sets the father with the MessageRecord father
     * @param father
     */
    public void setFather(MessageRecord father) {
        this.father = father;
    }

    /**
     * Gets the MessageRecord mother
     * @return mother
     */
    public MessageRecord getMother() {
        return mother;
    }

    /**
     * Sets the father with the MessageRecord mother
     * @param mother
     */
    public void setMother(MessageRecord mother) {
        this.mother = mother;
    }

    /**
     * Gets the string messageBody
     * @return messageBody
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * Sets the messageBody with the given string messageBody
     * @param messageBody
     */
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * Gets the Timestamp timestamp
     * @return timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp with the Timestamp timestamp
     * @param timestamp
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the byte[] signature
     * @return signature
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Sets the signature with the given byte[] signature
     * @param signature
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    /**
     * Get the MessageRecord set leftChildren
     * @return leftChildren
     */
    public Set<MessageRecord> getLeftChildren() {
        return leftChildren;
    }

    /**
     * Sets the leftChildren with the MessageRecord set leftChildren
     * @param leftChildren
     */
    public void setLeftChildren(Set<MessageRecord> leftChildren) {
        this.leftChildren = leftChildren;
    }

    /**
     * Get the MessageRecord set rightChildren
     * @return rightChildren
     */
    public Set<MessageRecord> getRightChildren() {
        return rightChildren;
    }

    /**
     * Sets the rightChildren with the MessageRecord set rightChildren
     * @param rightChildren
     */
    public void setRightChildren(Set<MessageRecord> rightChildren) {
        this.rightChildren = rightChildren;
    }

    /**
     * Converts MessageRecord to a MessageDatagram
     * @return new MessageDatagram
     */
    public MessageDatagram toDatagram() {
        return new MessageDatagram(this);
    }

    /**
     * Checks if the given MessageRecord is the root
     * @return true of is root
     */
    public boolean isRoot() {
        return getId().equals(new UUID(0, 0)) && getFather().getId().equals(getId()) && getMother().getId().equals(getId());
    }
}
