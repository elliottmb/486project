package edu.iastate.cs.theseguys.hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * The persistent class for the message database table.
 */
@Entity
@Table(name = "message")
public class Message {
    private UUID id;

    public Message() {

    }

    public Message(UUID id) {
        this.id = id;
    }

    @Id
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
