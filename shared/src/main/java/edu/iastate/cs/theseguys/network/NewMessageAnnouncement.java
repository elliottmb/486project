package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.hibernate.Message;

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
