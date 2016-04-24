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

import java.util.Arrays;

@Component
public class VerificationResponseHandler implements MessageHandler<VerificationResponse> {
    private static final Logger log = LoggerFactory.getLogger(VerificationResponseHandler.class);
    @Autowired
    private DatabaseManager databaseManager;

    @Autowired
    private AuthorityManager authorityManager;

    @Override
    public void handleMessage(IoSession session, VerificationResponse response) throws Exception {
        log.info("Received " + response.toString());

        MessageDatagram message = response.getMessage();

        ClientSecurity clientSecurity = new ClientSecurity();
        if (response.isValid() && clientSecurity.verifySignature(message.toSignable(), message.getSignature(), authorityManager.getPublicKey())) {
            log.info("Signature is " + Arrays.toString(message.getSignature()));
            databaseManager.getToProcess().push(new Pair<>(-1L, message));
        }
    }
}

