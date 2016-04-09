package edu.iastate.cs.theseguys;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.DemuxingIoHandler;

public class CustomDemuxingIoHandler extends DemuxingIoHandler {

	private AuthorityClientManager manager;
	
	public CustomDemuxingIoHandler(AuthorityClientManager manager)
	{
		super();
		this.manager = manager;
	}
	
	@Override
	public void sessionClosed(IoSession session)
	{
		
	}
	
	@Override
	public void sessionCreated(IoSession session)
	{
		
	}
	
	
}
