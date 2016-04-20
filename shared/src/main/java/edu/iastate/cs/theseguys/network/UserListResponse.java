package edu.iastate.cs.theseguys.network;

import java.util.List;

import edu.iastate.cs.theseguys.model.IDUsernamePair;

public class UserListResponse extends AbstractMessage {
	private static final long serialVersionUID = -8033950939300333602L;
	private List<IDUsernamePair> users;
	
	public UserListResponse(List<IDUsernamePair> users)
	{
		this.users = users;
	}
	
	public List<IDUsernamePair> getUsers()
	{
		return users;
	}

	
	public String toString()
	{
		String result = "";
		for(IDUsernamePair u : users)
		{
			result += u.getId() + " " + u.getUsername() + "\n";
		}
		return result;
	}
}
