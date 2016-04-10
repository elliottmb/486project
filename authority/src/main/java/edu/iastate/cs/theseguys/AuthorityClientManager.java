package edu.iastate.cs.theseguys;




import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.iastate.cs.theseguys.database.DatabaseManager;
import edu.iastate.cs.theseguys.network.LoginRequest;
import edu.iastate.cs.theseguys.network.NewMessageAnnouncement;

@Component
public class AuthorityClientManager {
	
	private static final Logger log = LoggerFactory.getLogger(AuthorityClientManager.class);
	private static final int PORT = 5050;
	
	private IoAcceptor acceptor;
	private CustomDemuxingIoHandler ioHandler;
	//iosession to port.  iosession contains client's ip, but we need to also store the port that the client listens for other clients on
	private ConcurrentMap<IoSession, Integer> connectedClients;  
	private ConcurrentMap<Long, IoSession> activeSessions;
	
	
	@Autowired
    private DatabaseManager databaseManager;
	
	public AuthorityClientManager()
	{
		connectedClients = new ConcurrentHashMap<IoSession, Integer>();
		activeSessions = new ConcurrentHashMap<Long, IoSession>();
		
		acceptor = new NioSocketAcceptor();
        //DemuxingIoHandler demuxIoHandler = new DemuxingIoHandler();
        ioHandler = new CustomDemuxingIoHandler(this);
        
        ioHandler.addReceivedMessageHandler(LoginRequest.class, new LoginRequestHandler(this));
        ioHandler.addReceivedMessageHandler(NewMessageAnnouncement.class, new NewMessageAnnouncementHandler(this));
        
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
	

	public void addNewClient(IoSession session, long userID, Integer port)
	{
		connectedClients.put(session, port);
		activeSessions.put(userID, session);
		
	}
	
	public void removeConnectedClient(IoSession session)
	{
		connectedClients.remove(session);
	}

	
	public boolean verifyLogin(String username, String password)
	{
		if (databaseManager.getRepository().userExists(username, password))
		{
			return true;
		}
		//if db contains this username/password combo, return true
		//else, return false
		
		return false;
	}
	
	public long getUserId(String username)
	{
		//get the id assigned to this user in the database
		return databaseManager.getRepository().getId(username);
	}
}
