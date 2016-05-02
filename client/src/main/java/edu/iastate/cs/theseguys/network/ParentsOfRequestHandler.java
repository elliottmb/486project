package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.DatabaseManager;
import edu.iastate.cs.theseguys.database.MessageRecord;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

@Component
/**
 * Handler for ParentsOfRequest
 *
 */
public class ParentsOfRequestHandler implements MessageHandler<ParentsOfRequest> {
    private static final Logger log = LoggerFactory.getLogger(ParentsOfRequestHandler.class);
    @Autowired
    private DatabaseManager databaseManager;


    @Override
    /**
     * Method gets the parents of the children in the request if and only if we do not already have it
     * @param session
     * @param request
     */
    public void handleMessage(IoSession session, ParentsOfRequest request) throws Exception {
        log.info("Received " + request.toString());

        Set<MessageDatagram> result = new LinkedHashSet<>();

        for (MessageDatagram messageDatagram : request.getChildren()) {
            MessageRecord fatherReoord = databaseManager.getRepository().findOne(messageDatagram.getFatherId());
            MessageRecord motherReoord = databaseManager.getRepository().findOne(messageDatagram.getMotherId());

            // Only add extant records and prevent loops
            if (fatherReoord != null && messageDatagram.getId() != fatherReoord.getId()) {
                result.add(fatherReoord.toDatagram());
            }
            if (motherReoord != null && messageDatagram.getId() != motherReoord.getId()) {
                result.add(motherReoord.toDatagram());
            }
        }

        session.write(new ParentsOfResponse(new LinkedList<>(result)));
    }
}

