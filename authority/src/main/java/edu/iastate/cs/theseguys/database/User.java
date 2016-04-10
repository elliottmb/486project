package edu.iastate.cs.theseguys.database;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@Column(columnDefinition  = "UUID NOT NULL UNIQUE")
	private UUID id;
	
	@Column(columnDefinition = "NVARCHAR(255) NOT NULL UNIQUE")
	private String username;
	
	@Column(columnDefinition = "")
	private String password;
	
	public User()
	{
		
	}
	
	public User(UUID id, String username, String password)
	{
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public String toString()
	{
		return String.format(
				"User[id='%s', username='%s', password='%s']",
				id,
				username,
				password
			);
	}
	
	public UUID getId()
	{
		return id;
	}
	
	public void setId(UUID id)
	{
		this.id = id;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
}
