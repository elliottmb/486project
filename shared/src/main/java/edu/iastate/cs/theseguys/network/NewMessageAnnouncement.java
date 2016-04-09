package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.hibernate.Message;

/**
 * When a Client has successfully created and verified a 
 * message with the central authority, this message is sent 
 * out to all the other clients the originating client is currently connected to.
 *
 */
public class NewMessageAnnouncement extends AbstractMessage {
    private static final long serialVersionUID = 8210961163089587206L;
    private Message message;

    public NewMessageAnnouncement(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NewMessageAnnouncement{" +
                "message=" + message +
                '}';
    }

    public Message getMessage() {
        return message;
    }

}
