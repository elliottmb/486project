package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.AuthorityManager;
import edu.iastate.cs.theseguys.distributed.ClientManager;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/**
 * Handler for a PeerListResponse
 *
 */
public class PeerListResponseHandler implements MessageHandler<PeerListResponse> {
    private static final Logger log = LoggerFactory.getLogger(PeerListResponseHandler.class);

    @Autowired
    private AuthorityManager authorityManager;
    @Autowired
    private ClientManager clientManager;

    @Override
    /**
     * Method that sets the peers of a client with the given response clients
     * @param session
     * @param response
     */
    public void handleMessage(IoSession session, PeerListResponse response) throws Exception {


        log.info("Received " + response);

        clientManager.setKnownPeers(response.getClients());

    }

}
