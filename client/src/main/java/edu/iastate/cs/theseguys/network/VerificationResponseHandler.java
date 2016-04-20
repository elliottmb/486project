package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.DatabaseManager;
import javafx.util.Pair;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VerificationResponseHandler implements MessageHandler<VerificationResponse> {
    private static final Logger log = LoggerFactory.getLogger(VerificationResponseHandler.class);
    @Autowired
    private DatabaseManager databaseManager;


    @Override
    public void handleMessage(IoSession session, VerificationResponse message) throws Exception {
        log.info("Received " + message.toString());

        MessageDatagram m = message.getMessage();

        if (message.isValid()) {
            databaseManager.getWaiting().push(new Pair<>(-1L, m));
        }
    }
}

