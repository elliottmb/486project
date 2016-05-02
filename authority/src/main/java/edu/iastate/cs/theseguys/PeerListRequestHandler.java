package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.model.Peer;
import edu.iastate.cs.theseguys.network.PeerListRequest;
import edu.iastate.cs.theseguys.network.PeerListResponse;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handle PeerListRequests sent by connected clients
 *
 */
@Component
public class PeerListRequestHandler implements MessageHandler<PeerListRequest> {
    @Autowired
    private AuthorityClientManager manager;

    
    /**
     * Handle the provided PeerListRequest sent through the provided session.
     * Write a response to the given session containing a list of all
     * clients currently connected to the authority.  
     */
    @Override
    public void handleMessage(IoSession session, PeerListRequest request) throws Exception {

        List<Peer> users = manager.getActivePeers(session);

        session.write(new PeerListResponse(users));
    }
}

