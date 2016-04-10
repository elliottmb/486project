package edu.iastate.cs.theseguys;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;

import edu.iastate.cs.theseguys.network.NewMessageAnnouncement;

public class NewMessageAnnouncementHandler implements MessageHandler<NewMessageAnnouncement> {

	private AuthorityClientManager manager;
	
	public NewMessageAnnouncementHandler(AuthorityClientManager manager)
	{
		super();
		this.manager = manager;
	}
	
    @Override
    public void handleMessage(IoSession session, NewMessageAnnouncement announcement) throws Exception {
    	//I guess we need to encode this message using the CentralAuthority's private key
    	//and send the client a response containing that value
    	
    	
    }
}

