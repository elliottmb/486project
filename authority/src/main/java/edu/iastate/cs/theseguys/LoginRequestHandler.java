package edu.iastate.cs.theseguys;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.iastate.cs.theseguys.network.LoginRequest;
import edu.iastate.cs.theseguys.network.LoginResponse;

public class LoginRequestHandler implements MessageHandler<LoginRequest> {

	private AuthorityClientManager manager;
	
	public LoginRequestHandler(AuthorityClientManager manager)
	{
		super();
		this.manager = manager;
	}
	
    @Override
    public void handleMessage(IoSession session, LoginRequest request) throws Exception {
    	
    	if (manager.verifyLogin(request.getUsername(), request.getPassword()))
    	{
    		long userID = manager.getUserId(request.getUsername());
    		manager.addNewClient(session, userID, request.getPort());
    		session.write(new LoginResponse(true, userID));
    	}
    	else
    	{
    		//reject this login attempt
    		session.write(new LoginResponse(false, 0));
    	}
    	
    }
}

