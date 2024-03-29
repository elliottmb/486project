package edu.iastate.cs.theseguys.network;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Class used primarily for testing.  Handles Messages but does nothing but print the message content to the log
 */
@Component
public class LoggingMessageHandler implements MessageHandler<AbstractMessage> {
    private static final Logger log = LoggerFactory.getLogger(LoggingMessageHandler.class);

    @Override
    public void handleMessage(IoSession session, AbstractMessage message) throws Exception {
        log.info(message.toString());
    }
}

