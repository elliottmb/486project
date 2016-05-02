package edu.iastate.cs.theseguys;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Custom class to gracefully handle clients that forcefully disconnect
 *
 */
public class CustomDemuxingIoHandler extends DemuxingIoHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomDemuxingIoHandler.class);
    private AuthorityClientManager manager;

    public CustomDemuxingIoHandler() {
        super();
    }

    /**
     * When the given session is closed, remove it from the map
     * of managed client sessions
     */
    @Override
    public void sessionClosed(IoSession session) {
        log.info("SESSION CLOSED");
        manager.removeUserSession(session);
        log.info("CONNECTED CLIENTS: " + manager.getConnectedClients());
    }


    public void setManager(AuthorityClientManager manager) {
        this.manager = manager;
    }
}
