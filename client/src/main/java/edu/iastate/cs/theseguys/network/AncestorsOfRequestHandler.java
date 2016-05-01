package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.DatabaseManager;
import edu.iastate.cs.theseguys.database.MessageRecord;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AncestorsOfRequestHandler implements MessageHandler<AncestorsOfRequest> {
    private static final Logger log = LoggerFactory.getLogger(AncestorsOfRequestHandler.class);
    @Autowired
    private DatabaseManager databaseManager;


    @Override
    public void handleMessage(IoSession session, AncestorsOfRequest request) throws Exception {
        log.info("Received " + request.toString());

        LinkedHashMap<UUID, MessageDatagram> ancestors = new LinkedHashMap<>();

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

            if (ancestors.containsKey(messageDatagram.getId())) {
                log.info("Already processed " + messageDatagram.getId());
                continue;
            }
            ancestors.put(messageDatagram.getId(), messageDatagram);

            if (ancestors.containsKey(messageDatagram.getFatherId())) {
                log.info("Already found father of " + messageDatagram.getId() + ": " + messageDatagram.getFatherId());
            } else {
                log.info("Looking for father of " + messageDatagram.getId() + ": " + messageDatagram.getFatherId());
                MessageRecord fatherReoord = databaseManager.getRepository().findOne(messageDatagram.getFatherId());

                if (fatherReoord != null && messageDatagram.getId() != fatherReoord.getId()) {
                    MessageDatagram fatherDatagram = fatherReoord.toDatagram();

                    if (!fatherReoord.isRoot()) {
                        temp.addLast(fatherDatagram);
                    }
                }
            }
            if (ancestors.containsKey(messageDatagram.getMotherId())) {
                log.info("Already found mother of " + messageDatagram.getId() + ": " + messageDatagram.getMotherId());
            } else {
                log.info("Looking for mother of " + messageDatagram.getId() + ": " + messageDatagram.getMotherId());
                MessageRecord motherReoord = databaseManager.getRepository().findOne(messageDatagram.getMotherId());
                if (motherReoord != null && messageDatagram.getId() != motherReoord.getId()) {
                    MessageDatagram motherDatagram = motherReoord.toDatagram();

                    if (!motherReoord.isRoot()) {
                        temp.addLast(motherDatagram);
                    }
                }
            }
        }

        List<MessageDatagram> results = ancestors.values()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        session.write(new AncestorsOfResponse(results));
    }
}

