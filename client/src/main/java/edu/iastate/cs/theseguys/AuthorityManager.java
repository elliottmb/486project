package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.network.*;
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

    public AuthorityManager() {
        super(new NioSocketConnector(), new DemuxingIoHandler());
    }

    public synchronized boolean isLoggedIn() {
        return userId != null;
    }

    @PostConstruct
    private void prepareHandlers() {
        getIoHandler().addSentMessageHandler(LoginRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(LoginResponse.class, loginResponseHandler); // TODO: Actually handle
        getIoHandler().addSentMessageHandler(RegisterRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(RegisterResponse.class, MessageHandler.NOOP); // TODO: Actually handle
        getIoHandler().addSentMessageHandler(VerificationRequest.class, MessageHandler.NOOP);
        getIoHandler().addReceivedMessageHandler(VerificationResponse.class, verificationResponseHandler);

        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
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
