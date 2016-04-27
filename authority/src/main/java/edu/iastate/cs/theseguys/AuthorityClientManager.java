package edu.iastate.cs.theseguys;


import edu.iastate.cs.theseguys.database.DatabaseManager;
import edu.iastate.cs.theseguys.model.Peer;
import edu.iastate.cs.theseguys.network.*;
import javafx.util.Pair;
import org.apache.mina.core.session.IoSession;
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
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
public class AuthorityClientManager extends AbstractIoAcceptorManager {

    private static final Logger log = LoggerFactory.getLogger(AuthorityClientManager.class);
    private static final int PORT = 9090;
    @Autowired
    private LoginRequestHandler loginRequestHandler;
    @Autowired
    private RegisterRequestHandler registerRequestHandler;
    @Autowired
    private PeerListRequestHandler peerListRequestHandler;
    @Autowired
    private UserListRequestHandler userListRequestHandler;
    // Map of IoSession to Authorized User ID and Listening Port
    private ConcurrentMap<IoSession, Pair<UUID, Integer>> activeSessions;
    @Autowired
    private DatabaseManager databaseManager;
    @Autowired
    private VerificationRequestHandler verificationRequestHandler;

    public AuthorityClientManager() {
        super(new NioSocketAcceptor(), new CustomDemuxingIoHandler());
        activeSessions = new ConcurrentHashMap<>();

        ((CustomDemuxingIoHandler) getIoHandler()).setManager(this);
    }

    public void run() throws IOException {
        bind(new InetSocketAddress(PORT));
    }

    @PostConstruct
    private void prepareHandlers() {
        getIoHandler().addReceivedMessageHandler(LoginRequest.class, loginRequestHandler);
        getIoHandler().addReceivedMessageHandler(RegisterRequest.class, registerRequestHandler);
        getIoHandler().addReceivedMessageHandler(PeerListRequest.class, peerListRequestHandler);
        getIoHandler().addReceivedMessageHandler(VerificationRequest.class, verificationRequestHandler);
        getIoHandler().addReceivedMessageHandler(UserListRequest.class, userListRequestHandler);

        getIoHandler().addSentMessageHandler(RegisterResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(PeerListResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(LoginResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(VerificationResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(UserListResponse.class, MessageHandler.NOOP);


        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }


    public void addUserSession(IoSession session, UUID userID, Integer port) {
        activeSessions.put(session, new Pair<>(userID, port));
    }

    public void removeUserSession(IoSession session) {
        activeSessions.remove(session);
    }

    public boolean verifyUserSession(IoSession session, UUID userId) {
        return userId.equals(activeSessions.get(session).getKey());
    }

    public List<Peer> getActivePeers(IoSession excluding) {
        return activeSessions.entrySet()
                .stream()
                .filter(
                        e -> !e.getKey().equals(excluding)
                )
                .map(
                        e -> new Peer(((InetSocketAddress) e.getKey().getRemoteAddress()).getAddress().getHostAddress(), e.getValue().getValue())
                )
                .collect(Collectors.toList());
    }


    public List<Peer> getActivePeers() {
        return activeSessions.entrySet()
                .stream()
                .map(
                        e -> new Peer(((InetSocketAddress) e.getKey().getRemoteAddress()).getAddress().getHostAddress(), e.getValue().getValue())
                )
                .collect(Collectors.toList());
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public String getConnectedClients() {
        String result = "";
        for (Entry<IoSession, Pair<UUID, Integer>> e : activeSessions.entrySet()) {
            result += e.getKey() + " " + e.getValue() + "\n";
        }
        return result;
    }

}
