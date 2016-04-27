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
public class ParentsOfResponseHandler implements MessageHandler<ParentsOfResponse> {
    private static final Logger log = LoggerFactory.getLogger(ParentsOfResponseHandler.class);
    @Autowired
    private DatabaseManager databaseManager;


    @Override
    public void handleMessage(IoSession session, ParentsOfResponse response) throws Exception {
        log.info("Received " + response.toString());

        for (MessageDatagram messageDatagram : response.getParents()) {
            databaseManager.getToProcess().push(new Pair<>(session.getId(), messageDatagram));
        }
    }
}
