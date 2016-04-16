package edu.iastate.cs.theseguys.network;

/**
 * Network response containing a single message.  Sent from
 * client B to client A whenever A sends a LatestMessageRequest
 * to B
 */
public class LatestMessageResponse extends AbstractMessage {
    private static final long serialVersionUID = -2575582116681604249L;
    private MessageDatagram message;

    public LatestMessageResponse(MessageDatagram message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LatestMessageResponse{" +
                "message=" + message +
                '}';
    }

    public MessageDatagram getMessage() {
        return this.message;
    }
}
