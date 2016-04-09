package edu.iastate.cs.theseguys;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.stereotype.Component;

@Component
public class AuthorityClientManager {
	
	private IoAcceptor acceptor;
	private CustomDemuxingIoHandler ioHandler;
	//iosession to port.  iosession contains client's ip, but we need to also store the port that the client listens for other clients
	private ConcurrentMap<IoSession, Integer> connectedClients;  
	
	public AuthorityClientManager()
	{
		acceptor = new NioSocketAcceptor();
        //DemuxingIoHandler demuxIoHandler = new DemuxingIoHandler();
        ioHandler = new CustomDemuxingIoHandler(this);
		
		connectedClients = new ConcurrentHashMap<IoSession, Integer>();
	}
	

}
