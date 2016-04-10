package edu.iastate.cs.theseguys.network;


public class LoginResponse extends AbstractMessage{
	private static final long serialVersionUID = -8424104713314817865L;
	private boolean success;
	private long assignedId;
	
	public LoginResponse(boolean success, long assignedId)
	{
		this.success = success;
		this.assignedId = assignedId;
	}
	
	public boolean getSuccess()
	{
		return success;
	}
	
	public long getAssignedId()
	{
		return assignedId;
	}
	
	
	

}
