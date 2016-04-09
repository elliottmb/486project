package edu.iastate.cs.theseguys.network;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LatestMessageRequestHandler implements MessageHandler<LatestMessageRequest> {
    private static final Logger log = LoggerFactory.getLogger(LatestMessageRequestHandler.class);

    @Override
    public void handleMessage(IoSession session, LatestMessageRequest message) throws Exception {
        log.info("Received " + message.toString());
    }
}

