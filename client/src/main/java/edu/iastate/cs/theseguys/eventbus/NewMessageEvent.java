package edu.iastate.cs.theseguys.eventbus;

import edu.iastate.cs.theseguys.database.MessageRecord;

public class NewMessageEvent extends MessageEvent{
	private final MessageRecord message;
	
	public MessageRecord getMessage() {
		return message;
	}

	public NewMessageEvent(Object source, MessageRecord message) {
		super(source);
		this.message = message;
	}
	
	@Override
    public String toString() {
        return "NewMessageEvent{}";
    }

}
