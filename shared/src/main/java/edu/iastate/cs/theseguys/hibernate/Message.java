package edu.iastate.cs.theseguys.hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

/**
 * The persistent class for the message database table.
 */
@Entity
@Table(name = "message")
public class Message implements Serializable {
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "father_id")
    private Set<Message> leftChildren;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "mother_id")
    private Set<Message> rightChildren;
    @ManyToOne
    private Message father;
    @ManyToOne
    private Message mother;
    @Id
    @Column(columnDefinition = "UUID NOT NULL UNIQUE")
    private UUID id;
    @Column(columnDefinition = "UUID NOT NULL")
    private UUID userId;
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String messageBody;
    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private Timestamp timestamp;
    @Column(columnDefinition = "BLOB NOT NULL")
    private byte[] signature;

    public Message() {

    }

    public Message(UUID id, UUID userId, String messageBody, Timestamp timestamp, byte[] signature) {
        this.id = id;
        this.userId = userId;
        this.messageBody = messageBody;
        this.timestamp = timestamp;
        this.signature = signature;
    }

    @Override
    public String toString() {
        return String.format(
                "Message[id='%s', userId='%s', timestamp='%s', body='%s', fatherId='%s', motherId='%s']",
                id,
                userId,
                timestamp,
                messageBody,
                father.getId(),
                mother.getId()
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

    public Message getFather() {
        return father;
    }

    public void setFather(Message father) {
        this.father = father;
    }

    public Message getMother() {
        return mother;
    }

    public void setMother(Message mother) {
        this.mother = mother;
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

    public Set<Message> getLeftChildren() {
        return leftChildren;
    }

    public void setLeftChildren(Set<Message> leftChildren) {
        this.leftChildren = leftChildren;
    }

    public Set<Message> getRightChildren() {
        return rightChildren;
    }

    public void setRightChildren(Set<Message> rightChildren) {
        this.rightChildren = rightChildren;
    }
}
