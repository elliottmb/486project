package edu.iastate.cs.theseguys;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;

import edu.iastate.cs.theseguys.model.Peer;
import edu.iastate.cs.theseguys.network.PeerListRequest;
import edu.iastate.cs.theseguys.network.PeerListResponse;


public class PeerListRequestHandler implements MessageHandler<PeerListRequest> {

	private AuthorityClientManager manager;
	
	public PeerListRequestHandler(AuthorityClientManager manager)
	{
		super();
		this.manager = manager;
	}
	
    @Override
    public void handleMessage(IoSession session, PeerListRequest request) throws Exception {
    	
    	List<Peer> users = manager.getAllClients();
    	
    	session.write(new PeerListResponse(users));
    	
    	
    }
}

