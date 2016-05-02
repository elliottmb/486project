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

/**
 * Manages connections to clients which are currently logged into
 * the system and handles incoming and outgoing messages from 
 * and to those clients
 */
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
    @Autowired
    private LogoutRequestHandler logoutRequestHandler;
    @Autowired
    private VerificationRequestHandler verificationRequestHandler;
    
    /**
     * Map the IoSession of a connected client to that client's 
     * authorized UserID and the port which that client is listening on
     */
    private ConcurrentMap<IoSession, Pair<UUID, Integer>> activeSessions;
    
    /**
     * Manages connection and calls to the local database
     */
    @Autowired
    private DatabaseManager databaseManager;
    
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
        getIoHandler().addReceivedMessageHandler(LogoutRequest.class, logoutRequestHandler);

        getIoHandler().addSentMessageHandler(RegisterResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(PeerListResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(LoginResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(VerificationResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(UserListResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(LogoutResponse.class, MessageHandler.NOOP);

        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }


    /**
     * Add a new client session to the map of currently active sessions
     * @param session Session corresponding to the new connection
     * @param userID Assigned UserId of the connecting client
     * @param port Port which this connected client is listening for other clients on
     */
    public void addUserSession(IoSession session, UUID userID, Integer port) {
        activeSessions.put(session, new Pair<>(userID, port));
    }

    /**
     * Remove the given session from the map of active sessions
     * @param session Session to remove
     */
    public void removeUserSession(IoSession session) {
        activeSessions.remove(session);
    }

    /**
     * Check whether the provided userID is attached to the given session
     * @param session Session to verify
     * @param userId User to match with session
     * @return True if the given userId is associated with the given session, false otherwise
     */
    public boolean verifyUserSession(IoSession session, UUID userId) {
        return userId.equals(activeSessions.get(session).getKey());
    }

    /**
     * Return a list of all connected clients except for any clients connected on the given session
     * @param excluding A session to exclude from the list
     * @return List of all connected clients excluding the one specified
     */
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


    /**
     * Get a list of all Peers currently connected to the centralAuthority
     * @return List of all connected Peers
     */
    public List<Peer> getActivePeers() {
        return activeSessions.entrySet()
                .stream()
                .map(
                        e -> new Peer(((InetSocketAddress) e.getKey().getRemoteAddress()).getAddress().getHostAddress(), e.getValue().getValue())
                )
                .collect(Collectors.toList());
    }

    /**
     * Return the authority's database manager
     * @return
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * Construct and return a string representation of all clients currently connected to the authority
     * @return
     */
    public String getConnectedClients() {
        String result = "";
        for (Entry<IoSession, Pair<UUID, Integer>> e : activeSessions.entrySet()) {
            result += e.getKey() + " " + e.getValue() + "\n";
        }
        return result;
    }

}
