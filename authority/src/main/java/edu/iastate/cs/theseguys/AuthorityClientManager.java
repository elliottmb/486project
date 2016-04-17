package edu.iastate.cs.theseguys;




import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.iastate.cs.theseguys.database.DatabaseManager;
import edu.iastate.cs.theseguys.model.Peer;
import edu.iastate.cs.theseguys.network.LoginRequest;
import edu.iastate.cs.theseguys.network.LoginResponse;
import edu.iastate.cs.theseguys.network.PeerListRequest;
import edu.iastate.cs.theseguys.network.PeerListResponse;
import edu.iastate.cs.theseguys.network.RegisterRequest;
import edu.iastate.cs.theseguys.network.RegisterResponse;
import edu.iastate.cs.theseguys.network.VerificationRequest;
import edu.iastate.cs.theseguys.network.VerificationResponse;

@Component
public class AuthorityClientManager {
	
	private static final Logger log = LoggerFactory.getLogger(AuthorityClientManager.class);
	private static final int PORT = 5050;
	
	private IoAcceptor acceptor;
	private CustomDemuxingIoHandler ioHandler;
	//iosession to port.  iosession contains client's ip, but we need to also store the port that the client listens for other clients on
	private ConcurrentMap<IoSession, Integer> connectedClients;  
	private ConcurrentMap<UUID, IoSession> activeSessions;
	
	
	@Autowired
    private DatabaseManager databaseManager;
	
	public AuthorityClientManager()
	{
		connectedClients = new ConcurrentHashMap<IoSession, Integer>();
		activeSessions = new ConcurrentHashMap<UUID, IoSession>();
		
		acceptor = new NioSocketAcceptor();
        ioHandler = new CustomDemuxingIoHandler(this);
        
        ioHandler.addReceivedMessageHandler(LoginRequest.class, new LoginRequestHandler(this));
        ioHandler.addReceivedMessageHandler(RegisterRequest.class, new RegisterRequestHandler(this));
        ioHandler.addReceivedMessageHandler(PeerListRequest.class, new PeerListRequestHandler(this));
        ioHandler.addReceivedMessageHandler(VerificationRequest.class, new VerificationRequestHandler(this));
        
        ioHandler.addSentMessageHandler(RegisterResponse.class, MessageHandler.NOOP);
        ioHandler.addSentMessageHandler(PeerListResponse.class, MessageHandler.NOOP);
        ioHandler.addSentMessageHandler(LoginResponse.class, MessageHandler.NOOP);
        ioHandler.addSentMessageHandler(VerificationResponse.class, MessageHandler.NOOP);
        
        
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        acceptor.setHandler(ioHandler);
        
        try{
        	acceptor.bind(new InetSocketAddress(PORT));
        }
        catch(IOException ioe)
        {
        	log.info("Failed to bind to port "+PORT);
        	ioe.printStackTrace();
        	
        }
       
		
		
		
	}
	

	public void addNewClient(IoSession session, UUID userID, Integer port)
	{
		connectedClients.put(session, port);
		activeSessions.put(userID, session);
		
	}
	
	public void removeConnectedClient(IoSession session)
	{
		connectedClients.remove(session);
	}

	
	
	
	
	
	public List<Peer> getAllClients()
	{
		List<Peer> clients= new ArrayList<Peer>(connectedClients.size());
		
		for (Entry<IoSession, Integer> e : connectedClients.entrySet())
		{
			clients.add(new Peer(e.getKey().getRemoteAddress().toString(), e.getValue()));
		}
		
		return clients;
	}
	
	public DatabaseManager getDatabaseManager()
	{
		return databaseManager;
	}
	
	public String getConnectedClients()
	{
		String result = "";
		for (Entry<IoSession, Integer> e : connectedClients.entrySet())
		{
			result+=e.getKey() + " " + e.getValue() + "\n";
		}
		return result;
	}

}
