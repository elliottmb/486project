package edu.iastate.cs.theseguys.eventbus;

import edu.iastate.cs.theseguys.database.MessageRecord;

/**
 * Event class used when a new message occurs
 *
 */
public class NewMessageEvent extends MessageEvent{
	private final MessageRecord message;
	
	/**
	 * Gets the MessageRecord message
	 * @return message
	 */
	public MessageRecord getMessage() {
		return message;
	}

	/**
	 * Creates a NewMessageEvent with the given Object source and MessageRecord message
	 * @param source
	 * @param message
	 */
	public NewMessageEvent(Object source, MessageRecord message) {
		super(source);
		this.message = message;
	}
	
	/**
	 * Returns the NewMessageEvent string
	 */
	@Override
    public String toString() {
        return "NewMessageEvent{}";
    }

}
