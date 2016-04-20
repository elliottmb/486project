package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.network.MessageDatagram;
import edu.iastate.cs.theseguys.network.VerificationRequest;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VerificationRequestHandler implements MessageHandler<VerificationRequest> {
    private static final Logger log = LoggerFactory.getLogger(VerificationRequestHandler.class);
    @Autowired
    private AuthorityClientManager manager;

    @Override
    public void handleMessage(IoSession session, VerificationRequest request) throws Exception {
        //I guess we need to encode this message using the CentralAuthority's private key
        //and send the client a response containing that value
        log.info("Received: " + request);

        MessageDatagram message = request.getMessage();


    }
}

