package edu.iastate.cs.theseguys.distributed;

import edu.iastate.cs.theseguys.AbstractIoConnectorManager;
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

@Component
public class ClientManager extends AbstractIoConnectorManager {
    private static final Logger log = LoggerFactory.getLogger(ClientManager.class);

    @Autowired
    private ServerManager serverManager;
    @Autowired
    private LoggingMessageHandler loggingMessageHandler;

    @Autowired
    private LatestMessageResponseHandler latestMessageResponseHandler;
    @Autowired
    private ParentsOfResponseHandler parentsOfResponseHandler;

    public ClientManager() {
        super(new NioSocketConnector(), new ClientDemuxingIoHandler());


    }

    @PostConstruct
    private void prepareHandlers() {
        getIoHandler().addReceivedMessageHandler(NewMessageAnnouncement.class, loggingMessageHandler);
        getIoHandler().addSentMessageHandler(LatestMessageRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(LatestMessageResponse.class, latestMessageResponseHandler);
        getIoHandler().addSentMessageHandler(ParentsOfRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(ParentsOfResponse.class, parentsOfResponseHandler);

        ((ManagedDemuxingIoHandler) getIoHandler()).setClientManager(this);
        ((ManagedDemuxingIoHandler) getIoHandler()).setServerManager(serverManager);


        /*
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            SslFilter sslFilter = new SslFilter(sslContext);
            getService().getFilterChain().addFirst("sslFilter", sslFilter);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.warn("Failed to establish SSLFitler");
            e.printStackTrace();
        }
        */
        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }
}
