package edu.iastate.cs.theseguys;

import java.util.UUID;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import edu.iastate.cs.theseguys.database.User;
import edu.iastate.cs.theseguys.network.RegisterRequest;
import edu.iastate.cs.theseguys.network.RegisterResponse;

public class RegisterRequestHandler implements MessageHandler<RegisterRequest> {
    private static final Logger log = LoggerFactory.getLogger(RegisterRequestHandler.class);
   
	private AuthorityClientManager manager;
	
	public RegisterRequestHandler(AuthorityClientManager manager)
	{
		super();
		this.manager = manager;
	}
	
    @Override
    public void handleMessage(IoSession session, RegisterRequest request) throws Exception {
    	
    	log.info("Received a register request:"+request.getUsername() + " " + request.getPassword());
    	
    	if (manager.getDatabaseManager().userExists(request.getUsername()))
    	{
    		log.info("User already exists");
    		session.write(new RegisterResponse(false, "Username already taken"));
    	}
    	else
    	{
    		User newUser = new User(UUID.randomUUID(), request.getUsername(), request.getPassword());
    		manager.getDatabaseManager().insertUser(newUser);
    		
    		session.write(new RegisterResponse(true, "Account creation successful"));
    	}
    	
    	log.info("Users currently in database: ");
        for (User user : manager.getDatabaseManager().getRepository().findAll()) {
            log.info(user.toString());
        }
    }
}

