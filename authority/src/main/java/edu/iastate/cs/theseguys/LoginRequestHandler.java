package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.database.User;
import edu.iastate.cs.theseguys.network.LoginRequest;
import edu.iastate.cs.theseguys.network.LoginResponse;
import edu.iastate.cs.theseguys.security.AuthoritySecurity;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LoginRequestHandler implements MessageHandler<LoginRequest> {

    private static final Logger log = LoggerFactory.getLogger(LoginRequestHandler.class);
    @Autowired
    private AuthorityClientManager manager;

    @Override
    public void handleMessage(IoSession session, LoginRequest request) throws Exception {
        // Will return a user object if authentication is successful, otherwise returns null
        User u = manager.getDatabaseManager().authenticateUser(request.getUsername(), request.getPassword());

        if (u != null) {
            log.info("Login successful");
            UUID userID = u.getId();
            manager.addNewClient(session, userID, request.getPort());
            AuthoritySecurity authSec = new AuthoritySecurity();
            session.write(new LoginResponse(true, userID, authSec.getPublicKey()));
        } else {
            log.info("Login Failed");
            //reject this login attempt
            session.write(new LoginResponse(false, new UUID(0, 0), null));
        }

        log.info("CONNECTED CLIENTS: " + manager.getConnectedClients());
    }
}

