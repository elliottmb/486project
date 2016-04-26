package edu.iastate.cs.theseguys.network;

import java.net.InetSocketAddress;
import java.util.Map.Entry;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.iastate.cs.theseguys.distributed.ClientManager;

@Component
public class PeerConnectionRequestHandler implements MessageHandler<PeerConnectionRequest> {

    private static final Logger log = LoggerFactory.getLogger(PeerConnectionRequestHandler.class);

	
	@Autowired
	private ClientManager clientManager;
	
	@Override
	public void handleMessage(IoSession session, PeerConnectionRequest message) throws Exception {
		
		log.info("PEER CONNECTION REQUEST RECEIVED WITH SESSION ID:"+message.getSessionId()+".  LISTING ALL CONNECTED CLIENTS");
		
		for(Entry<Long, IoSession> io : clientManager.getService().getManagedSessions().entrySet())
		{
			System.out.println("CONNECTED CLIENT SESSIONID: "+io.getKey());
		}
		
		ConnectFuture peerFuture;
		try {
			//if we are not already connected to the requesting client.  
			if (!clientManager.getService().getManagedSessions().containsKey(message.getSessionId()))
			{
				peerFuture = clientManager.connect(new InetSocketAddress(((InetSocketAddress)session.getRemoteAddress()).getHostName(), message.getServerPort()));
	            peerFuture.awaitUninterruptibly();
			}  
        } catch (RuntimeIoException e) {
            log.warn("Failed to connect to peer");
            e.printStackTrace();
        }
		
		//clientManager.connect(new InetSocketAddress(((InetSocketAddress)session.getLocalAddress()).getHostName(), message.getServerPort()));
		
	}

}
