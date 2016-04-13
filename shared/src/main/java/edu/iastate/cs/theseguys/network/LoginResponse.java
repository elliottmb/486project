package edu.iastate.cs.theseguys.network;

import java.util.UUID;

public class LoginResponse extends AbstractMessage{
	private static final long serialVersionUID = -8424104713314817865L;
	private boolean success;
	private UUID assignedId;  //this is the id which the user's name/password are associated with in the Users table
	
	public LoginResponse(boolean success, UUID assignedId)
	{
		this.success = success;
		this.assignedId = assignedId;
	}
	
	public boolean getSuccess()
	{
		return success;
	}
	
	public UUID getAssignedId()
	{
		return assignedId;
	}
	
	
	

}
