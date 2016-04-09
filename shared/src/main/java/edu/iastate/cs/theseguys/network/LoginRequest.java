package edu.iastate.cs.theseguys.network;

import java.io.Serializable;

public class LoginRequest extends AbstractMessage {
	private static final long serialVersionUID = -100458915682459958L;
	private String username;
	private String password;
	
	
	public LoginRequest(String username, String password)
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
