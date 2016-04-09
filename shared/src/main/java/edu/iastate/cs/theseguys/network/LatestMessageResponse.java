package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.hibernate.Message;

/**
 * Network response containing a single message.  Sent from
 * client B to client A whenever A sends a LatestMessageRequest
 * to B
 *
 */
public class LatestMessageResponse extends AbstractMessage {
    private static final long serialVersionUID = -2575582116681604249L;
    private Message message;

    public LatestMessageResponse(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return this.message;
    }
}
