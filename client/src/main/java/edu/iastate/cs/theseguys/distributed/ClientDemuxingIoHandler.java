package edu.iastate.cs.theseguys.distributed;


import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import edu.iastate.cs.theseguys.network.PeerConnectionRequest;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientDemuxingIoHandler extends ManagedDemuxingIoHandler {

    private static final Logger log = LoggerFactory.getLogger(ClientDemuxingIoHandler.class);

	
    @Override
    public void sessionCreated(IoSession session) throws Exception {
    	
    	
        session.write(new LatestMessageRequest());

        InetSocketAddress addr = (InetSocketAddress)getServerManager().getService().getLocalAddress();
        log.info("SENDING PEER CONNECTION REQUEST with port:"+addr.getPort()+" and sessionId: "+session.getId());

        session.write(new PeerConnectionRequest(addr.getPort(), session.getId()));

    }

}
