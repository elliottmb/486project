package edu.iastate.cs.theseguys;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomDemuxingIoHandler extends DemuxingIoHandler {

	private AuthorityClientManager manager;
	private static final Logger log = LoggerFactory.getLogger(CustomDemuxingIoHandler.class);

	
	public CustomDemuxingIoHandler(AuthorityClientManager manager)
	{
		super();
		this.manager = manager;
	}
	
	@Override
	public void sessionClosed(IoSession session)
	{
		log.info("SESSION CLOSED");
		manager.removeConnectedClient(session);
		log.info("CONNECTED CLIENTS: "+manager.getConnectedClients());
	}
	
	@Override
	public void sessionCreated(IoSession session)
	{
		
	}
	
	
}
