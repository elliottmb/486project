package edu.iastate.cs.theseguys.network;

import java.io.Serializable;

public class LoginResponse implements Serializable {
	private static final long serialVersionUID = -8424104713314817865L;
	private boolean success;
	
	public LoginResponse(boolean success)
	{
		this.success = success;
	}
	
	public boolean getSuccess()
	{
		return success;
	}
	
	
	

}
