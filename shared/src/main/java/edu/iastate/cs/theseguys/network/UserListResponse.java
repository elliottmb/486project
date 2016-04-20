package edu.iastate.cs.theseguys.network;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;


public class UserListResponse extends AbstractMessage {
	private static final long serialVersionUID = -8033950939300333602L;
	private Map<UUID, String> users;
	
	public UserListResponse(Map<UUID, String> users)
	{
		this.users = users;
	}
	
	public Map<UUID, String> getUsers()
	{
		return users;
	}

	
	public String toString()
	{
		String result = "";
		for(Entry<UUID, String> u : users.entrySet())
		{
			result += u.getKey() + " " + u.getValue() + "\n";
		}
		return result;
	}
}
