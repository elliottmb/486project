package edu.iastate.cs.theseguys.network;


public class RegisterRequest extends AbstractMessage {
	private static final long serialVersionUID = -7803515103821448698L;
	private String username;
	private String password;
	
	
	public RegisterRequest(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}

}
