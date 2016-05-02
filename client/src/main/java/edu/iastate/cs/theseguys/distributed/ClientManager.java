package edu.iastate.cs.theseguys.distributed;

import edu.iastate.cs.theseguys.AbstractIoConnectorManager;
import edu.iastate.cs.theseguys.model.Peer;
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
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
/**
 * Manager class for Client
 *
 */
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
    @Autowired
    private NewMessageAnnouncementHandler newMessageAnnouncementHandler;
    @Autowired
    private AncestorsOfResponseHandler ancestorsOfResponseHandler;


    private List<Peer> knownPeers;

    /**
     * Creates a ClientManager with a new NioSocketConnector and new ClientDemuxingIoHandler
     * Sets knownPeers to a new LinkedList
     */
    public ClientManager() {
        super(new NioSocketConnector(), new ClientDemuxingIoHandler());

        knownPeers = new LinkedList<>();
    }

    /**
     * Gets Peer list knownPeers
     * @return
     */
    public synchronized List<Peer> getKnownPeers() {
        return knownPeers;
    }

    /**
     * Method for connecting to all peers that are known but not connected.
     * 
     * @param peers
     */
    public synchronized void setKnownPeers(List<Peer> peers) {
        knownPeers = peers;

        List<InetSocketAddress> connectionAddresses = getService().getManagedSessions()
                .entrySet()
                .stream()
                .map(
                        e -> (InetSocketAddress) e.getValue().getRemoteAddress()
                )
                .collect(Collectors.toList());

        List<Peer> notCurrentlyConnected = knownPeers.stream()
                .filter(
                        e -> connectionAddresses.stream().noneMatch(c -> c.getAddress().getHostAddress().equals(e.getIp()) && c.getPort() == e.getPort())
                )
                .collect(Collectors.toList());

        log.info(knownPeers.toString());
        log.info(notCurrentlyConnected.toString());

        for (Peer peer : notCurrentlyConnected) {
            getService().connect(new InetSocketAddress(peer.getIp(), peer.getPort()));
        }
    }

    @PostConstruct
    /**
     * We set the handlers for our manager here.
     * Sent message handlers are given NOOP since we don't take any action with them
     * otherwise we set a defined handler to them
     */
    private void prepareHandlers() {
        getIoHandler().addReceivedMessageHandler(NewMessageAnnouncement.class, newMessageAnnouncementHandler);
        getIoHandler().addSentMessageHandler(LatestMessageRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(LatestMessageResponse.class, latestMessageResponseHandler);
        getIoHandler().addSentMessageHandler(ParentsOfRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(ParentsOfResponse.class, parentsOfResponseHandler);
        getIoHandler().addSentMessageHandler(PeerConnectionRequest.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(AncestorsOfRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(AncestorsOfResponse.class, ancestorsOfResponseHandler);

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
