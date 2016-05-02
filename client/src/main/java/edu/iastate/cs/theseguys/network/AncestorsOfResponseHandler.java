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
 * Handler for AncestorsOfResponse
 *
 */
public class AncestorsOfResponseHandler implements MessageHandler<AncestorsOfResponse> {
    private static final Logger log = LoggerFactory.getLogger(AncestorsOfResponseHandler.class);
    @Autowired
    private DatabaseManager databaseManager;


    @Override
    /**
     * For each MessageDatagram in the responses ancestors we push the id, messageDatagram pair to the queue to be processed
     * @param session
     * @param response
     */
    public void handleMessage(IoSession session, AncestorsOfResponse response) throws Exception {
        log.info("Received " + response.toString());

        for (MessageDatagram messageDatagram : response.getAncestors()) {
            databaseManager.getToProcess().push(new Pair<>(session.getId(), messageDatagram));
        }
    }
}

