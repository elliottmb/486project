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


/**
 * Handle LoginRequests sent by connected clients
 *
 */
@Component
public class LoginRequestHandler implements MessageHandler<LoginRequest> {

    private static final Logger log = LoggerFactory.getLogger(LoginRequestHandler.class);
    @Autowired
    private AuthorityClientManager manager;

    /**
     * Handle the provided LoginRequest sent over the given session.
     * Check if the login request is from a registered user.
     * If it is, respond with a LoginResponse containing the authority's public key
     * Otherwise, reject the login attempt and write a null LoginResponse to the client
     */
    @Override
    public void handleMessage(IoSession session, LoginRequest request) throws Exception {
        // Will return a user object if authentication is successful, otherwise returns null
        User u = manager.getDatabaseManager().authenticateUser(request.getUsername(), request.getPassword());

        if (u != null) {
            log.info("Login successful");
            UUID userID = u.getId();
            manager.addUserSession(session, userID, request.getPort());
            AuthoritySecurity authSec = new AuthoritySecurity();
            session.write(new LoginResponse(true, userID, authSec.getPublicKey()));
        } else {
            log.info("Login Failed");
            //reject this login attempt
            session.write(new LoginResponse(false, new UUID(0, 0), null));
        }
    }
}

