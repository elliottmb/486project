package edu.iastate.cs.theseguys.network;

import java.security.PublicKey;
import java.util.UUID;

public class LoginResponse extends AbstractMessage{
	private static final long serialVersionUID = -8424104713314817865L;
	private boolean success;
	private UUID assignedId;  //this is the id which the user's name/password are associated with in the Users table
	private PublicKey pubKey;
	
	public LoginResponse(boolean success, UUID assignedId, PublicKey key)
	{
		this.success = success;
		this.assignedId = assignedId;
		this.pubKey = key;
	}
	
	public boolean getSuccess()
	{
		return success;
	}
	
	public UUID getAssignedId()
	{
		return assignedId;
	}
	
	public PublicKey getPublicKey() {
		return pubKey;
	}
	

}
