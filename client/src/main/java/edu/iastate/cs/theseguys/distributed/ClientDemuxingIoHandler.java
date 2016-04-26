package edu.iastate.cs.theseguys.distributed;


import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import edu.iastate.cs.theseguys.network.PeerConnectionRequest;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IoSession;

public class ClientDemuxingIoHandler extends ManagedDemuxingIoHandler {

    @Override
    public void sessionCreated(IoSession session) throws Exception {
    	
    	
        session.write(new LatestMessageRequest());
        session.write(new PeerConnectionRequest(((InetSocketAddress)getServerManager().getService().getLocalAddress()).getPort()));
    }

}
