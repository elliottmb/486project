package edu.iastate.cs.theseguys;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomDemuxingIoHandler extends DemuxingIoHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomDemuxingIoHandler.class);
    private AuthorityClientManager manager;

    public CustomDemuxingIoHandler() {
        super();
    }

    @Override
    public void sessionClosed(IoSession session) {
        log.info("SESSION CLOSED");
        manager.removeUserSession(session);
        log.info("CONNECTED CLIENTS: " + manager.getConnectedClients());
    }

    @Override
    public void sessionCreated(IoSession session) {

    }


    public void setManager(AuthorityClientManager manager) {
        this.manager = manager;
    }
}
