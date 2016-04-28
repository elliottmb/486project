package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.network.LogoutRequest;
import edu.iastate.cs.theseguys.network.LogoutResponse;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class LogoutRequestHandler implements MessageHandler<LogoutRequest> {

    private static final Logger log = LoggerFactory.getLogger(LogoutRequestHandler.class);
    @Autowired
    private AuthorityClientManager manager;

    @Override
    public void handleMessage(IoSession session, LogoutRequest request) throws Exception {
    	
    	manager.removeUserSession(session);
    	session.write(new LogoutResponse(true));


        log.info("CONNECTED CLIENTS: " + manager.getConnectedClients());
    }
}

