package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.hibernate.Message;



/**
 * Request the direct parents of the provided list of
 * child Messages.
 * 
 *
 */
public class VerificationResponse extends AbstractMessage {
	private static final long serialVersionUID = -7719439373013433851L;
	private Message message;
	private boolean valid;

    public VerificationResponse(Message message, boolean valid) {
    	this.message = message;
    	this.valid = valid;
    }

    public Message getMessage() {
        return message;
    }
    
    public boolean getValid()
    {
    	return valid;
    }
}
