package edu.iastate.cs.theseguys;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import edu.iastate.cs.theseguys.network.LatestMessageRequestHandler;
import edu.iastate.cs.theseguys.network.LoggingMessageHandler;
import edu.iastate.cs.theseguys.network.NewMessageAnnouncement;

/**
 * The CentralAuthority will be responsible for validating Messages
 * from a Client before it can send that Message to other Clients.
 * Additionally, maintains connections to all currently operating
 * Clients, so that new Clients know where to connect to in order to
 * build up their peer network.  
 */
public class CentralAuthority {
	
	private static final Logger log = LoggerFactory.getLogger(CentralAuthority.class);
	private static AuthorityClientManager clientManager;
	
    public static void main(String[] args) throws IOException {

    	
    	
        System.out.println("I touch others");
        
        
        
        //put these in the clientManager
        
        
        clientManager = new AuthorityClientManager();

//        demuxIoHandler.addReceivedMessageHandler(LatestMessageRequest.class, new LatestMessageRequestHandler());
//        demuxIoHandler.addReceivedMessageHandler(NewMessageAnnouncement.class, new LoggingMessageHandler());
//
//        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
//        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
//        acceptor.setHandler(demuxIoHandler);
//        acceptor.bind(new InetSocketAddress(5050));
        
        
        
        
        
        
        
        
        
    }

    public boolean someLibraryMethod() {

        return true;
    }
}
