package edu.iastate.cs.theseguys.network;

/**
 * Network response containing a single message.  Sent from
 * client B to client A whenever A sends a LatestMessageRequest
 * to B
 */
public class LatestMessageResponse extends AbstractMessage {
    private static final long serialVersionUID = -2575582116681604249L;
    private MessageDatagram message;

    /**
     * Creates a LatestMessageResponse with the given MessageDatagram message
     * @param message
     */
    public LatestMessageResponse(MessageDatagram message) {
        this.message = message;
    }

    /**
     * Returns the MessageDatagram message as a LatestMessageResponse string
     */
    @Override
    public String toString() {
        return "LatestMessageResponse{" +
                "message=" + message +
                '}';
    }

    /**
     * Gets the MessageDatagram message
     * @return message
     */
    public MessageDatagram getMessage() {
        return this.message;
    }
}
