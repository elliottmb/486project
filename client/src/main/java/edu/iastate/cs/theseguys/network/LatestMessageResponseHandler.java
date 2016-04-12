package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.DatabaseManager;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LatestMessageResponseHandler implements MessageHandler<LatestMessageResponse> {
    private static final Logger log = LoggerFactory.getLogger(LatestMessageResponseHandler.class);
    @Autowired
    DatabaseManager databaseManager;


    @Override
    public void handleMessage(IoSession session, LatestMessageResponse message) throws Exception {
        log.info("Received " + message.toString());

        MessageDatagram m = message.getMessage();

        // TODO:
    }
}

