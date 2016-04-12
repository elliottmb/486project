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
public class LatestMessageRequestHandler implements MessageHandler<LatestMessageRequest> {
    private static final Logger log = LoggerFactory.getLogger(LatestMessageRequestHandler.class);
    @Autowired
    private DatabaseManager databaseManager;


    @Override
    public void handleMessage(IoSession session, LatestMessageRequest message) throws Exception {
        log.info("Received " + message.toString());

        MessageRecord latestMessage = databaseManager.getLatestMessage();

        MessageDatagram messageDatagram = new MessageDatagram(latestMessage);

        session.write(new LatestMessageResponse(messageDatagram));
    }
}

