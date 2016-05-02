package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.AuthorityManager;
import edu.iastate.cs.theseguys.DatabaseManager;
import edu.iastate.cs.theseguys.security.ClientSecurity;
import javafx.util.Pair;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/**
 * Handler for LatestMessageResponse
 *
 */
public class LatestMessageResponseHandler implements MessageHandler<LatestMessageResponse> {
    private static final Logger log = LoggerFactory.getLogger(LatestMessageResponseHandler.class);
    @Autowired
    private DatabaseManager databaseManager;
    @Autowired
    private AuthorityManager authorityManager;


    @Override
    /**
     * Method that gets the message from the response and verifies it. If verified we push the session id and message to the ToProcess queue
     * @param session
     * @param response
     */
    public void handleMessage(IoSession session, LatestMessageResponse response) throws Exception {
        log.info("Received " + response.toString());

        MessageDatagram message = response.getMessage();

        ClientSecurity clientSecurity = new ClientSecurity();
        if (clientSecurity.verifySignature(message.toSignable(), message.getSignature(), authorityManager.getPublicKey())) {
            databaseManager.getToProcess().push(new Pair<>(session.getId(), message));
        } else {
            log.warn("Received an invalid message");
        }
    }
}

