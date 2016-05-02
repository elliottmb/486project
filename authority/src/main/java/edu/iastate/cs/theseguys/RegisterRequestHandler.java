package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.database.User;
import edu.iastate.cs.theseguys.network.RegisterRequest;
import edu.iastate.cs.theseguys.network.RegisterResponse;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Handle RegisterRequests sent by connected clients
 *
 */
@Component
public class RegisterRequestHandler implements MessageHandler<RegisterRequest> {
    private static final Logger log = LoggerFactory.getLogger(RegisterRequestHandler.class);

    @Autowired
    private AuthorityClientManager manager;

    /**
     * Handle the given RegisterRequest sent over the given session.
     * If the request contains a valid username and password, create a new 
     * row in the Users database and respond with a successful RegisterResponse.
     * If the request contains invalid or previously taken username/password,
     * write a failed RegisterResponse to the session
     */
    @Override
    public void handleMessage(IoSession session, RegisterRequest request) throws Exception {
        log.info("Received a register request:" + request.getUsername() + " " + request.getPassword());

        if (request.getUsername() == null || request.getUsername().length() < 3) {
            log.info("Username too short");
            session.write(new RegisterResponse(false, "Username must be at least 3 characters long."));
            return;
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            log.info("Password too short");
            session.write(new RegisterResponse(false, "Password must be at least 6 characters long."));
            return;
        }
        if (manager.getDatabaseManager().userExists(request.getUsername())) {
            log.info("User already exists");
            session.write(new RegisterResponse(false, "Username already taken"));
            return;
        }

        User newUser = new User(UUID.randomUUID(), request.getUsername(), request.getPassword());
        manager.getDatabaseManager().insertUser(newUser);

        session.write(new RegisterResponse(true, "Account creation successful"));
    }
}

