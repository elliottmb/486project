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
/**
 * Handler class for ParentsOfResponse
 *
 */
public class ParentsOfResponseHandler implements MessageHandler<ParentsOfResponse> {
    private static final Logger log = LoggerFactory.getLogger(ParentsOfResponseHandler.class);
    @Autowired
    private DatabaseManager databaseManager;


    @Override
    /**
     * For each Message in the responses parents we push the id, message pair into the queue
     * @param session
     * @param response
     */
    public void handleMessage(IoSession session, ParentsOfResponse response) throws Exception {
        log.info("Received " + response.toString());

        for (MessageDatagram messageDatagram : response.getParents()) {
            databaseManager.getToProcess().push(new Pair<>(session.getId(), messageDatagram));
        }
    }
}

