package edu.iastate.cs.theseguys.distributed;

import edu.iastate.cs.theseguys.AbstractIoConnectorManager;
import edu.iastate.cs.theseguys.DatabaseManager;
import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.network.*;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class ClientManager extends AbstractIoConnectorManager {
    private static final Logger log = LoggerFactory.getLogger(ClientManager.class);

    @Autowired
    private LoggingMessageHandler loggingMessageHandler;
    @Autowired
    private DatabaseManager databaseManager;

    private Deque<MessageDatagram> waiting;
    private Deque<MessageDatagram> ready;
    @Autowired
    private LatestMessageResponseHandler latestMessageResponseHandler;

    public ClientManager() {
        super(new NioSocketConnector(), new ClientDemuxingIoHandler());

        waiting = new ConcurrentLinkedDeque<>();
        ready = new ConcurrentLinkedDeque<>();
    }

    public synchronized void processQueue() {
        if (!waiting.isEmpty()) {
            MessageDatagram head = waiting.pop();

            // Make sure it doesn't already exist
            if (!databaseManager.getRepository().exists(head.getId())) {
                // Check if we have both the father and mother already
                if (databaseManager.getRepository().exists(head.getFatherId()) && databaseManager.getRepository().exists(head.getMotherId())) {
                   // If we do, move this guy to the ready queue
                    ready.add(head);
                } else {
                    // TODO:
                    waiting.add(head);
                }
            }
        }
        if (!ready.isEmpty()) {

        }
    }

    @PostConstruct
    private void prepareHandlers() {
        getIoHandler().addSentMessageHandler(LatestMessageRequest.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(NewMessageAnnouncement.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(LatestMessageResponse.class, latestMessageResponseHandler);
        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }
}
