package edu.iastate.cs.theseguys.network;

public class PeerDisconnectionResponse extends AbstractMessage{
	private static final long serialVersionUID = -386870733093546674L;
	private boolean success;
	
	
	public PeerDisconnectionResponse(boolean success)
	{
		this.success = success;
	}
	
	public boolean isSuccess()
	{
		return success;
	}
	
}
