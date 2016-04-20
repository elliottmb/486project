package edu.iastate.cs.theseguys;


import edu.iastate.cs.theseguys.database.DatabaseManager;
import edu.iastate.cs.theseguys.model.Peer;
import edu.iastate.cs.theseguys.network.*;
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


    //iosession to port.  iosession contains client's ip, but we need to also store the port that the client listens for other clients on
    private ConcurrentMap<IoSession, Integer> connectedClients;
    private ConcurrentMap<UUID, IoSession> activeSessions;


    @Autowired
    private DatabaseManager databaseManager;

    public AuthorityClientManager() {
        super(new NioSocketAcceptor(), new CustomDemuxingIoHandler());
        connectedClients = new ConcurrentHashMap<IoSession, Integer>();
        activeSessions = new ConcurrentHashMap<UUID, IoSession>();

        ((CustomDemuxingIoHandler) getIoHandler()).setManager(this);
    }

    public void run() throws IOException {
        bind(new InetSocketAddress(PORT));
    }

    @PostConstruct
    private void prepareHandlers() {
        getIoHandler().addReceivedMessageHandler(LoginRequest.class, new LoginRequestHandler(this));
        getIoHandler().addReceivedMessageHandler(RegisterRequest.class, new RegisterRequestHandler(this));
        getIoHandler().addReceivedMessageHandler(PeerListRequest.class, new PeerListRequestHandler(this));
        getIoHandler().addReceivedMessageHandler(VerificationRequest.class, new VerificationRequestHandler(this));
        getIoHandler().addReceivedMessageHandler(UserListRequest.class, new UserListRequestHandler(this));

        getIoHandler().addSentMessageHandler(RegisterResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(PeerListResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(LoginResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(VerificationResponse.class, MessageHandler.NOOP);
        getIoHandler().addSentMessageHandler(UserListResponse.class, MessageHandler.NOOP);


        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }


    public void addNewClient(IoSession session, UUID userID, Integer port) {
        connectedClients.put(session, port);
        activeSessions.put(userID, session);

    }

    public void removeConnectedClient(IoSession session) {
        connectedClients.remove(session);
    }


    public List<Peer> getAllClients() {
        return connectedClients.entrySet()
                .stream()
                .map(
                        e -> new Peer(e.getKey().getRemoteAddress().toString(), e.getValue())
                )
                .collect(Collectors.toList());
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public String getConnectedClients() {
        String result = "";
        for (Entry<IoSession, Integer> e : connectedClients.entrySet()) {
            result += e.getKey() + " " + e.getValue() + "\n";
        }
        return result;
    }

}
