package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.network.LogoutRequest;
import edu.iastate.cs.theseguys.network.LogoutResponse;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handle LogoutRequests sent by connected clients
 *
 */
@Component
public class LogoutRequestHandler implements MessageHandler<LogoutRequest> {

    private static final Logger log = LoggerFactory.getLogger(LogoutRequestHandler.class);
    @Autowired
    private AuthorityClientManager manager;

    /**
     * Handle LogoutRequests from the given session.
     * Remove the session from the list of logged in clients
     * and write a LogoutResponse to the client confirming the logout
     */
    @Override
    public void handleMessage(IoSession session, LogoutRequest request) throws Exception {
    	
    	manager.removeUserSession(session);
    	session.write(new LogoutResponse(true));


        log.info("CONNECTED CLIENTS: " + manager.getConnectedClients());
    }
}

