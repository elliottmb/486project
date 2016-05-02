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
/**
 * Manager class for Authority. Contains basic initialization/handler assignment
 *
 */
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
    
    /**
     * Creates a basic AuthorityManager with a new NioSocketConnector and DemuxingIoHandler
     */
    public AuthorityManager() {
        super(new NioSocketConnector(), new DemuxingIoHandler());
    }

    /**
     * If userId is not null we return true
     * @return userId != null
     */
    public synchronized boolean isLoggedIn() {
        return userId != null;
    }

    @PostConstruct
    /**
     * We set all of our message handlers for sent/received. NOOP used when no action needs to be taken, otherwise
     * an instance is given.
     */
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


    /**
     * Method for verifying signature of a given MessageDatagram message
     * @param message
     * @return true if the message is verified
     */
    public synchronized boolean verifySignature(MessageDatagram message) {
        return verifySignature(message.toSignable(), message.getSignature());
    }

    /**
     * Method for verifiying a signature of a given string message with given byte[] signature
     * @param message
     * @param sig
     * @return true if the signature is verified
     */
    public synchronized boolean verifySignature(String message, byte[] sig) {
        return clientSecurity.verifySignature(message, sig, getPublicKey());
    }

    /**
     * Gets the UUID userID
     * @return userId
     */
    public synchronized UUID getUserId() {
        return userId;
    }

    /**
     * Sets the UUID userId
     * @param userId
     */
    public synchronized void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * Gets the PublicKey publicKey
     * @return publicKey
     */
    public synchronized PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Sets the PublicKey publicKey
     * @param publicKey
     */
    public synchronized void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }


}
