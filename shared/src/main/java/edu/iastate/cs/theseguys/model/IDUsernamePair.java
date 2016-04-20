package edu.iastate.cs.theseguys.model;

import java.io.Serializable;
import java.util.UUID;

public class IDUsernamePair implements Serializable{
	private static final long serialVersionUID = -6970529708698128962L;
	private UUID id;
	private String username;
	
	public IDUsernamePair(UUID id, String username)
	{
		this.id = id;
		this.username = username;
		
	}
	
	public UUID getId()
	{
		return id;
	}
	
	public String getUsername()
	{
		return username;
	}
}
