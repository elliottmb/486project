package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.model.Peer;
import edu.iastate.cs.theseguys.network.PeerListRequest;
import edu.iastate.cs.theseguys.network.PeerListResponse;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PeerListRequestHandler implements MessageHandler<PeerListRequest> {
    @Autowired
    private AuthorityClientManager manager;

    @Override
    public void handleMessage(IoSession session, PeerListRequest request) throws Exception {

        List<Peer> users = manager.getActivePeers();

        session.write(new PeerListResponse(users));
    }
}

