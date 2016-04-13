package edu.iastate.cs.theseguys.network;


public class RegisterResponse extends AbstractMessage {
	private static final long serialVersionUID = 6116255766837004104L;
	private boolean success;
	private String message;
	
	
	public RegisterResponse(boolean success, String message)
	{
		this.success = success;
		this.message = message;
	}

	public boolean getSuccess()
	{
		return success;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	@Override
	public String toString()
	{
		return "success: " + success + "  message:"+message;
	}
	

}
