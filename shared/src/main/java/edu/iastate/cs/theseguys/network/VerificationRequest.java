package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.hibernate.Message;



/**
 * Request the direct parents of the provided list of
 * child Messages.
 * 
 *
 */
public class VerificationRequest extends AbstractMessage {
	private static final long serialVersionUID = -7719439373013433851L;
	private Message message;

    public VerificationRequest(Message message) {
    	this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
