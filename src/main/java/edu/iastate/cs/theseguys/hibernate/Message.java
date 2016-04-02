package edu.iastate.cs.theseguys.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * The persistent class for the message database table.
 */
@Entity
@Table(name = "message")
public class Message implements Serializable {
    @Id
    @Column(columnDefinition = "UUID NOT NULL UNIQUE")
    private UUID id;
    @Column(columnDefinition = "UUID NOT NULL")
    private UUID userId;
    @Column(columnDefinition = "UUID NOT NULL")
    private UUID fatherId;
    @Column(columnDefinition = "UUID NOT NULL")
    private UUID motherId;
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String messageBody;
    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private Timestamp timestamp;
    @Column(columnDefinition = "BLOB NOT NULL")
    private byte[] signature;

    public Message() {

    }

    public Message(UUID id, UUID userId, UUID fatherId, UUID motherId, String messageBody, Timestamp timestamp, byte[] signature) {
        this.id = id;
        this.userId = userId;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.messageBody = messageBody;
        this.timestamp = timestamp;
        this.signature = signature;
    }

    @Override
    public String toString() {
        return String.format(
                "Message[id='%s', userId='%s', fatherId='%s', motherId='%s', body='%s']",
                id,
                userId,
                fatherId,
                motherId,
                messageBody
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getFatherId() {
        return fatherId;
    }

    public void setFatherId(UUID fatherId) {
        this.fatherId = fatherId;
    }

    public UUID getMotherId() {
        return motherId;
    }

    public void setMotherId(UUID motherId) {
        this.motherId = motherId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }


}
