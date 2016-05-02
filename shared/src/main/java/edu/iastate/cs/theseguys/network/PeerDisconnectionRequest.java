package edu.iastate.cs.theseguys.network;

/**
 * AbstractMessage extension used by clients to request a disconnection from another client
 * 
 */
public class PeerDisconnectionRequest extends AbstractMessage {
	private static final long serialVersionUID = -58334784425528727L;
	
	/**
	 * Creates a basic PeerDisconnectRequest
	 */
	public PeerDisconnectionRequest()
	{
		
	}

}
