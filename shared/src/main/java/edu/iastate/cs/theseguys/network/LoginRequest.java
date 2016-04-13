package edu.iastate.cs.theseguys.network;


public class LoginRequest extends AbstractMessage {
	private static final long serialVersionUID = -100458915682459958L;
	private String username;
	private String password;
	private int port;
	
	
	public LoginRequest(String username, String password, int port)
	{
		this.username = username;
		this.password = password;
		this.port = port;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public int getPort()
	{
		return port;
	}

}
