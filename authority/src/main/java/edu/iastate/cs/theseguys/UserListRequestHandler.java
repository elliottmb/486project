package edu.iastate.cs.theseguys;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;

import edu.iastate.cs.theseguys.model.IDUsernamePair;
import edu.iastate.cs.theseguys.network.UserListRequest;
import edu.iastate.cs.theseguys.network.UserListResponse;


public class UserListRequestHandler implements MessageHandler<UserListRequest> {

	private AuthorityClientManager manager;
	
	public UserListRequestHandler(AuthorityClientManager manager)
	{
		super();
		this.manager = manager;
	}
	
    @Override
    public void handleMessage(IoSession session, UserListRequest request) throws Exception {
    	List<IDUsernamePair> users = manager.getDatabaseManager().getAllUsers();
    	
    	session.write(new UserListResponse(users));
    }
}

