package edu.iastate.cs.theseguys.distributed;


import org.apache.mina.core.session.IoSession;

/**
 * Handler for ServerDemuxingIo
 *
 */
public class ServerDemuxingIoHandler extends ManagedDemuxingIoHandler {

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        // TODO: Connect to the connecting client if we're not already paired up
        //getClientManager().connect((InetSocketAddress) session.getRemoteAddress());
    }
}
