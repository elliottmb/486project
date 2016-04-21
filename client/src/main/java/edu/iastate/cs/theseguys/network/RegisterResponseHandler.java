package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.eventbus.RegisterEvent;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class RegisterResponseHandler implements MessageHandler<RegisterResponse> {
    private static final Logger log = LoggerFactory.getLogger(RegisterResponseHandler.class);
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void handleMessage(IoSession session, RegisterResponse message) throws Exception {
        log.info("Received " + message.toString());

        applicationEventPublisher.publishEvent(new RegisterEvent(this, message.isSuccess(), message.getMessage()));
    }
}

