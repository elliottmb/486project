package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.AuthorityManager;
import edu.iastate.cs.theseguys.eventbus.LoginEvent;
import edu.iastate.cs.theseguys.eventbus.LogoutEvent;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class LogoutResponseHandler implements MessageHandler<LogoutResponse> {
    private static final Logger log = LoggerFactory.getLogger(LogoutResponseHandler.class);
    
    @Autowired
    AuthorityManager authorityManager;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void handleMessage(IoSession session, LogoutResponse message) throws Exception {
        log.info("Received " + message.toString());

        if (message.isConfirmed()) {
        	
            CloseFuture closeFuture = session.closeOnFlush();
            closeFuture.awaitUninterruptibly();
            
            authorityManager.setPublicKey(null);
            authorityManager.setUserId(null);
            
            //somewhere we need to disconnect from all the other peers. not sure if should be done right here,
            //or by the LogoutEvent below?
        }

        applicationEventPublisher.publishEvent(new LogoutEvent(this, message.isConfirmed()));
    }
}

