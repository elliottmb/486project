package edu.iastate.cs.theseguys.distributed;

import edu.iastate.cs.theseguys.AbstractIoConnectorManager;
import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import edu.iastate.cs.theseguys.network.LatestMessageResponse;
import edu.iastate.cs.theseguys.network.LoggingMessageHandler;
import edu.iastate.cs.theseguys.network.NewMessageAnnouncement;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ClientManager extends AbstractIoConnectorManager {
    private static final Logger log = LoggerFactory.getLogger(ClientManager.class);

    @Autowired
    LoggingMessageHandler loggingMessageHandler;

    public ClientManager() {
        super(new NioSocketConnector(), new ClientDemuxingIoHandler());
    }

    @PostConstruct
    private void prepareHandlers() {
        getIoHandler().addSentMessageHandler(LatestMessageRequest.class, loggingMessageHandler);
        getIoHandler().addSentMessageHandler(NewMessageAnnouncement.class, loggingMessageHandler);
        getIoHandler().addReceivedMessageHandler(LatestMessageResponse.class, loggingMessageHandler);
        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }
}
