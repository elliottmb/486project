package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.network.MessageDatagram;
import edu.iastate.cs.theseguys.network.VerificationRequest;
import edu.iastate.cs.theseguys.network.VerificationResponse;
import edu.iastate.cs.theseguys.security.AuthoritySecurity;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;

/**
 * Handle VerificationRequests from connected clients
 *
 */
@Component
public class VerificationRequestHandler implements MessageHandler<VerificationRequest> {
    private static final Logger log = LoggerFactory.getLogger(VerificationRequestHandler.class);
    @Autowired
    private AuthorityClientManager manager;

    /**
     * Handle the given VerificationRequest from the provided IoSession
     * Check if the VerificationRequest is from a verified active session.
     * If it is, sign the message using the authority's private key and 
     * write the signed message back to the session in a VerificationResponse
     * If the message is not from a verified active session, write the message
     * back to the session in a failed VerificationResponse
     */
    @Override
    public void handleMessage(IoSession session, VerificationRequest request) throws Exception {
        log.info("Received: " + request);

        MessageDatagram message = request.getMessage();

        if (manager.verifyUserSession(session, message.getUserId())) {
            log.info("Message is from genuine session");
            AuthoritySecurity authoritySecurity = new AuthoritySecurity();
            PrivateKey privateKey = authoritySecurity.getPrivateKey();
            byte[] signature = authoritySecurity.generateSignature(message.toSignable(), privateKey);
            MessageDatagram signedMessage = new MessageDatagram(message, signature);
            session.write(new VerificationResponse(signedMessage, true));
        } else {
            log.info("Message is not from genuine session");
            session.write(new VerificationResponse(message, false));
        }

    }
}

