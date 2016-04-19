package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.AuthorityManager;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginResponseHandler implements MessageHandler<LoginResponse> {
    private static final Logger log = LoggerFactory.getLogger(LoginResponseHandler.class);
    @Autowired
    AuthorityManager authorityManager;


    @Override
    public void handleMessage(IoSession session, LoginResponse message) throws Exception {
        log.info("Received " + message.toString());

        if (message.isSuccess()) {
            authorityManager.setUserId(message.getAssignedId());
            authorityManager.setPublicKey(message.getPublicKey());
        }
    }
}

