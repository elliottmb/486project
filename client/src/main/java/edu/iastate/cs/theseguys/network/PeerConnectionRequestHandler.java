package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.distributed.ClientManager;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class PeerConnectionRequestHandler implements MessageHandler<PeerConnectionRequest> {
    private static final Logger log = LoggerFactory.getLogger(PeerConnectionRequestHandler.class);

    @Autowired
    private ClientManager clientManager;

    @Override
    public void handleMessage(IoSession session, PeerConnectionRequest request) throws Exception {
        InetSocketAddress sessionAddress = (InetSocketAddress) session.getRemoteAddress();

        InetSocketAddress sessionServerAddress = new InetSocketAddress(sessionAddress.getHostName(), request.getServerPort());

        log.info("Received " + request + " from " + sessionAddress);

        ConnectFuture peerFuture;
        try {
            //if we are not already connected to the requesting client.
            boolean alreadyConnected = clientManager.getService()
                    .getManagedSessions()
                    .entrySet()
                    .stream()
                    .anyMatch(
                            e -> {
                                InetSocketAddress eAddress = (InetSocketAddress) e.getValue().getRemoteAddress();
                                return eAddress.equals(sessionServerAddress);
                            }
                    );
            if (!alreadyConnected) {
                peerFuture = clientManager.connect(sessionServerAddress);
                peerFuture.awaitUninterruptibly();
            }
        } catch (RuntimeIoException e) {
            log.warn("Failed to connect to peer");
            e.printStackTrace();
        }
    }

}
