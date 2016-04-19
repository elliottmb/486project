package edu.iastate.cs.theseguys;

import java.util.UUID;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.iastate.cs.theseguys.database.User;
import edu.iastate.cs.theseguys.network.LoginRequest;
import edu.iastate.cs.theseguys.network.LoginResponse;
import edu.iastate.cs.theseguys.security.AuthoritySecurity;

public class LoginRequestHandler implements MessageHandler<LoginRequest> {

	private AuthorityClientManager manager;
	private static final Logger log = LoggerFactory.getLogger(LoginRequestHandler.class);

	
	public LoginRequestHandler(AuthorityClientManager manager)
	{
		super();
		this.manager = manager;
	}
	
    @Override
    public void handleMessage(IoSession session, LoginRequest request) throws Exception {
    	
    	User u = null;
    	
    	//will return a user object if authentication is successful, otherwise returns null
    	if ((u=manager.getDatabaseManager().authenticateUser(request.getUsername(), request.getPassword()))!=null)
    	{
    		log.info("Login successful");
    		UUID userID = u.getId();
    		manager.addNewClient(session, userID, request.getPort());
    		AuthoritySecurity authSec = new AuthoritySecurity();
    		session.write(new LoginResponse(true, userID, authSec.getPublicKey()));
    		
    	}
    	else
    	{
    		log.info("Login Failed");
    		//reject this login attempt
    		session.write(new LoginResponse(false, new UUID(0, 0), null));
    	}
    	
    	log.info("CONNECTED CLIENTS: " + manager.getConnectedClients());
    	
    }
}

