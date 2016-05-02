package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.DatabaseManager;
import edu.iastate.cs.theseguys.database.MessageRecord;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/**
 * Handler for LatestMessageRequest
 *
 */
public class LatestMessageRequestHandler implements MessageHandler<LatestMessageRequest> {
    private static final Logger log = LoggerFactory.getLogger(LatestMessageRequestHandler.class);
    @Autowired
    private DatabaseManager databaseManager;


    @Override
    /**
     * Method that gets the latest MessageRecord from the database manager and then writes it to the session
     * @param session
     * @param message
     */
    public void handleMessage(IoSession session, LatestMessageRequest message) throws Exception {
        log.info("Received " + message.toString());

        MessageRecord latestMessage = databaseManager.getLatestMessage();

        session.write(new LatestMessageResponse(latestMessage.toDatagram()));
    }
}

