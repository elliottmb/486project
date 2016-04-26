package edu.iastate.cs.theseguys.network;

public class PeerConnectionRequest extends AbstractMessage {
	private static final long serialVersionUID = -462688520305483484L;
	
	private int serverPort;
	private long sessionId;
	
	public PeerConnectionRequest(int serverPort, long sessionId)
	{
		this.serverPort = serverPort;
	}
	
	public int getServerPort()
	{
		return serverPort;
	}

	public long getSessionId()
	{
		return sessionId;
	}
}
