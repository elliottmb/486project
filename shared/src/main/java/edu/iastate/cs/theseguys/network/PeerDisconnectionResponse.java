package edu.iastate.cs.theseguys.network;

/**
 * AbstractMessage extension used by clients to respond to a PeerDisconnectRequest
 * 
 */
public class PeerDisconnectionResponse extends AbstractMessage{
	private static final long serialVersionUID = -386870733093546674L;
	private boolean success;
	
	/**
	 * Creates a PeerDisconnectResponse with the given success boolean
	 * @param success
	 */
	public PeerDisconnectionResponse(boolean success)
	{
		this.success = success;
	}
	
	/**
	 * Returns success
	 * @return success
	 */
	public boolean isSuccess()
	{
		return success;
	}
	
}
