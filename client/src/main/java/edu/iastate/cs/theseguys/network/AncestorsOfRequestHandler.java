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

        LinkedList<MessageDatagram> temp = new LinkedList<>();

        for (MessageDatagram messageDatagram : request.getChildren()) {
            log.info("looking for direct parents of " + messageDatagram.getId() + "( " + messageDatagram.getMotherId() + ", " + messageDatagram.getFatherId() + " )");

            MessageRecord fatherReoord = databaseManager.getRepository().findOne(messageDatagram.getFatherId());
            MessageRecord motherReoord = databaseManager.getRepository().findOne(messageDatagram.getMotherId());

            temp.add(fatherReoord.toDatagram());
            temp.add(motherReoord.toDatagram());
        }

        while (!temp.isEmpty()) {

            MessageDatagram messageDatagram = temp.pollFirst();

            if (result.contains(messageDatagram)) {
                log.info("Already processed " + messageDatagram.getId());
                continue;
            }
            result.add(messageDatagram);

            log.info("looking for parents of " + messageDatagram.getId() + "( " + messageDatagram.getMotherId() + ", " + messageDatagram.getFatherId() + " )");

            MessageRecord fatherReoord = databaseManager.getRepository().findOne(messageDatagram.getFatherId());
            MessageRecord motherReoord = databaseManager.getRepository().findOne(messageDatagram.getMotherId());

            if (fatherReoord != null && messageDatagram.getId() != fatherReoord.getId()) {
                MessageDatagram fatherDatagram = fatherReoord.toDatagram();

                if (!fatherReoord.isRoot() && !result.contains(fatherDatagram)) {
                    temp.addLast(fatherDatagram);
                }
            }
            if (motherReoord != null && messageDatagram.getId() != motherReoord.getId()) {
                MessageDatagram motherDatagram = motherReoord.toDatagram();

                if (!motherReoord.isRoot() && !result.contains(motherDatagram)) {
                    temp.addLast(motherDatagram);
                }
            }
        }

        session.write(new AncestorsOfResponse(new LinkedList<>(result)));
    }
}

