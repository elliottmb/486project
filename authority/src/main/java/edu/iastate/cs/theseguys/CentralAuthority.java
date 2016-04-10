package edu.iastate.cs.theseguys;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.iastate.cs.theseguys.database.DatabaseManager;


/**
 * The CentralAuthority will be responsible for validating Messages
 * from a Client before it can send that Message to other Clients.
 * Additionally, maintains connections to all currently operating
 * Clients, so that new Clients know where to connect to in order to
 * build up their peer network.  
 */
public class CentralAuthority {
	
	private static final Logger log = LoggerFactory.getLogger(CentralAuthority.class);
	
	@Autowired
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
