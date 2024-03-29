package edu.iastate.cs.theseguys.distributed;


import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import edu.iastate.cs.theseguys.network.PeerConnectionRequest;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Handler class for ClientDemuxingIo
 *
 */
public class ClientDemuxingIoHandler extends ManagedDemuxingIoHandler {

    private static final Logger log = LoggerFactory.getLogger(ClientDemuxingIoHandler.class);


    @Override
    /**
     * Method writes a new LatestMessageRequest to the session and writes a new PeerConnectionRequest
     * @param session
     */
    public void sessionCreated(IoSession session) throws Exception {
        session.write(new LatestMessageRequest());

        InetSocketAddress addr = (InetSocketAddress) getServerManager().getService().getLocalAddress();
        log.info("SENDING PEER CONNECTION REQUEST with port: " + addr.getPort());

        session.write(new PeerConnectionRequest(addr.getPort()));

    }

}
