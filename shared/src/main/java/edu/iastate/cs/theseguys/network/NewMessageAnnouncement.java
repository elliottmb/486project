package edu.iastate.cs.theseguys.network;

/**
 * When a Client has successfully created and verified a
 * message with the central authority, this message is sent
 * out to all the other clients the originating client is currently connected to.
 */
public class NewMessageAnnouncement extends AbstractMessage {
    private static final long serialVersionUID = 8210961163089587206L;
    private MessageDatagram message;

    /**
     * Creates a new NewMessageAnnouncement with the given MessageDatagram message
     * @param message
     */
    public NewMessageAnnouncement(MessageDatagram message) {
        this.message = message;
    }

    /**
     * Returns the MessageDatagram message as a NewMessageAnnouncement string
     */
    @Override
    public String toString() {
        return "NewMessageAnnouncement{" +
                "message=" + message +
                '}';
    }

    /**
     * Gets the MessageDatagram message
     * @return
     */
    public MessageDatagram getMessage() {
        return message;
    }

}
