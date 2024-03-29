package edu.iastate.cs.theseguys.distributed;

import edu.iastate.cs.theseguys.AbstractIoAcceptorManager;
import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.network.*;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;

@Component
/**
 * Manager class for server. Defines handlers and initial port.
 */
public class ServerManager extends AbstractIoAcceptorManager {
    private static final Logger log = LoggerFactory.getLogger(ServerManager.class);
    private int port;

    @Autowired
    private Client client;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private LatestMessageRequestHandler latestMessageRequestHandler;
    @Autowired
    private LoggingMessageHandler loggingMessageHandler;
    @Autowired
    private ParentsOfRequestHandler parentsOfRequestHandler;
    @Autowired
    private PeerConnectionRequestHandler peerConnectionRequestHandler;
    @Autowired
    private AncestorsOfRequestHandler ancestorsOfRequestHandler;

    /**
     * Creates a ServerManager with a new NioSocketAcceptor and ServerDemuxingIoHandler.
     * Sets port to 5050
     */
    public ServerManager() {
        super(new NioSocketAcceptor(), new ServerDemuxingIoHandler());

        port = 5050;
    }

    @PostConstruct
    /**
     * We prepare our message handlers here. Sent messages don't need action taken so we set NOOP. Otherwise
     * we give it a defined handler
     */
    private void prepareHandlers() {
        getIoHandler().addSentMessageHandler(NewMessageAnnouncement.class, loggingMessageHandler);
        getIoHandler().addReceivedMessageHandler(LatestMessageRequest.class, latestMessageRequestHandler);
        getIoHandler().addSentMessageHandler(LatestMessageResponse.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(ParentsOfRequest.class, parentsOfRequestHandler);
        getIoHandler().addSentMessageHandler(ParentsOfResponse.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(AncestorsOfRequest.class, ancestorsOfRequestHandler);
        getIoHandler().addSentMessageHandler(AncestorsOfResponse.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(PeerConnectionRequest.class, peerConnectionRequestHandler);

        ((ManagedDemuxingIoHandler) getIoHandler()).setClientManager(clientManager);
        ((ManagedDemuxingIoHandler) getIoHandler()).setServerManager(this);


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

    /**
     * Method for trying to bind on a port. We cycle through the first 99 in attempt to connect
     * @throws IOException
     * @throws InterruptedException
     */
    public void run() throws IOException, InterruptedException {
        // Try the first 99, catching the exceptions
        for (int i = 0; i < 100; i++) {
            try {
                bind(new InetSocketAddress(port));
                log.info("Client Producing Server bound on port " + port);
                break;
            } catch (IOException e) {
                // If we are here, and this one also fails, throw the exception
                if (i == 99) {
                    log.info("Failed to bind on port range, quitting");
                    throw e;
                }
                log.info("Failed to bind on port " + port + ", attempting +1");
                port++;
            }
        }
    }
}


