package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.AuthorityManager;
import edu.iastate.cs.theseguys.DatabaseManager;
import javafx.util.Pair;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewMessageAnnouncementHandler implements MessageHandler<NewMessageAnnouncement> {
    private static final Logger log = LoggerFactory.getLogger(NewMessageAnnouncementHandler.class);
    @Autowired
    private DatabaseManager databaseManager;
    @Autowired
    private AuthorityManager authorityManager;


    @Override
    public void handleMessage(IoSession session, NewMessageAnnouncement announcement) throws Exception {
        log.info("Received " + announcement.toString());

        MessageDatagram message = announcement.getMessage();

        if (authorityManager.verifySignature(message)) {
            databaseManager.getToProcess().push(new Pair<>(session.getId(), message));
        } else {
            log.warn("Received an invalid message");
        }
    }
}

