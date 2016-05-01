package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.network.*;
import edu.iastate.cs.theseguys.security.ClientSecurity;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PublicKey;
import java.util.UUID;

@Component
public class AuthorityManager extends AbstractIoConnectorManager {
    private static final Logger log = LoggerFactory.getLogger(AuthorityManager.class);

    private UUID userId;
    private PublicKey publicKey;
    @Autowired
    private LoginResponseHandler loginResponseHandler;
    @Autowired
    private VerificationResponseHandler verificationResponseHandler;
    @Autowired
    private RegisterResponseHandler registerResponseHandler;
    @Autowired
    private PeerListResponseHandler peerListResponseHandler;
    @Autowired
    private UserListResponseHandler userListResponseHandler;
    @Autowired
    private LogoutResponseHandler logoutResponseHandler;
    @Autowired
    private ClientSecurity clientSecurity;
    
    public AuthorityManager() {
        super(new NioSocketConnector(), new DemuxingIoHandler());
    }

    public synchronized boolean isLoggedIn() {
        return userId != null;
    }

    @PostConstruct
    private void prepareHandlers() {
        getIoHandler().addSentMessageHandler(LoginRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(LoginResponse.class, loginResponseHandler);
        getIoHandler().addSentMessageHandler(RegisterRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(RegisterResponse.class, registerResponseHandler);
        getIoHandler().addSentMessageHandler(VerificationRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(VerificationResponse.class, verificationResponseHandler);
        getIoHandler().addSentMessageHandler(PeerListRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(PeerListResponse.class, peerListResponseHandler);
        getIoHandler().addSentMessageHandler(UserListRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(UserListResponse.class, userListResponseHandler);
        getIoHandler().addSentMessageHandler(LogoutRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(LogoutResponse.class, logoutResponseHandler);

        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }


    public synchronized boolean verifySignature(MessageDatagram message) {
        return verifySignature(message.toSignable(), message.getSignature());
    }

    public synchronized boolean verifySignature(String message, byte[] sig) {
        return clientSecurity.verifySignature(message, sig, getPublicKey());
    }

    public synchronized UUID getUserId() {
        return userId;
    }

    public synchronized void setUserId(UUID userId) {
        this.userId = userId;
    }

    public synchronized PublicKey getPublicKey() {
        return publicKey;
    }

    public synchronized void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }


}
