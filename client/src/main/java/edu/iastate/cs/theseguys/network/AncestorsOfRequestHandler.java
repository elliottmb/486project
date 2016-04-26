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
public class AncestorsOfRequestHandler implements MessageHandler<AncestorsOfRequest> {
    private static final Logger log = LoggerFactory.getLogger(AncestorsOfRequestHandler.class);
    @Autowired
    private DatabaseManager databaseManager;


    @Override
    public void handleMessage(IoSession session, AncestorsOfRequest request) throws Exception {
        log.info("Received " + request.toString());

        Set<MessageDatagram> result = new LinkedHashSet<>();

        LinkedList<MessageDatagram> temp = new LinkedList<>(request.getChildren());

        while (!temp.isEmpty()) {

            MessageDatagram messageDatagram = temp.pollFirst();
            log.info("looking for parents of " + messageDatagram.getId());

            MessageRecord fatherReoord = databaseManager.getRepository().findOne(messageDatagram.getFatherId());
            MessageRecord motherReoord = databaseManager.getRepository().findOne(messageDatagram.getMotherId());

            if (fatherReoord != null && messageDatagram.getId() != fatherReoord.getId()) {
                MessageDatagram fatherDatagram = fatherReoord.toDatagram();
                result.add(fatherDatagram);

                if (!fatherReoord.isRoot()) {
                    temp.addLast(fatherDatagram);
                }
            }
            if (motherReoord != null && messageDatagram.getId() != motherReoord.getId()) {
                MessageDatagram motherDatagram = motherReoord.toDatagram();
                result.add(motherDatagram);

                if (!motherReoord.isRoot()) {
                    temp.addLast(motherDatagram);
                }
            }
        }

        session.write(new AncestorsOfResponse(new LinkedList<>(result)));
    }
}

