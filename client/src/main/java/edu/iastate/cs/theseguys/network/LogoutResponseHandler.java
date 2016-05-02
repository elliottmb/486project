package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.AuthorityManager;
import edu.iastate.cs.theseguys.distributed.ClientManager;
import edu.iastate.cs.theseguys.distributed.ServerManager;
import edu.iastate.cs.theseguys.eventbus.LogoutEvent;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
/**
 * Handler for LogoutResponse
 *
 */
public class LogoutResponseHandler implements MessageHandler<LogoutResponse> {
    private static final Logger log = LoggerFactory.getLogger(LogoutResponseHandler.class);

    @Autowired
    AuthorityManager authorityManager;
    @Autowired
    ClientManager clientManager;
    @Autowired
    ServerManager serverManager;


    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    /**
     * If the response is confirmed we clear out the userId and the public key. 
     * We then close off all connections of the client and server manager and publish the event
     * @param session
     * @param response
     */
    public void handleMessage(IoSession session, LogoutResponse response) throws Exception {
        log.info("Received " + response.toString());

        if (response.isConfirmed()) {

            authorityManager.setPublicKey(null);
            authorityManager.setUserId(null);

            clientManager.getService().getManagedSessions().forEach(
                    (e, b) -> {
                        b.closeOnFlush();
                    }
            );

            serverManager.getService().getManagedSessions().forEach(
                    (e, b) -> {
                        b.closeOnFlush();
                    }
            );

        }

        applicationEventPublisher.publishEvent(new LogoutEvent(this, response.isConfirmed()));
    }
}

