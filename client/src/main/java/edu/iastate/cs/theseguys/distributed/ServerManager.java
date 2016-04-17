package edu.iastate.cs.theseguys.distributed;

import edu.iastate.cs.theseguys.AbstractIoAcceptorManager;
import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.network.*;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;

@Component
public class ServerManager extends AbstractIoAcceptorManager {
    private static final Logger log = LoggerFactory.getLogger(ServerManager.class);
    private int port;

    @Autowired
    private Client client;
    @Autowired
    private LatestMessageRequestHandler latestMessageRequestHandler;
    @Autowired
    private LoggingMessageHandler loggingMessageHandler;

    public ServerManager() {
        super(new NioSocketAcceptor(), new ServerDemuxingIoHandler());

        port = 5050;
    }

    @PostConstruct
    private void prepareHandlers() {
        getIoHandler().addReceivedMessageHandler(LatestMessageRequest.class, latestMessageRequestHandler);
        getIoHandler().addReceivedMessageHandler(NewMessageAnnouncement.class, loggingMessageHandler);
        getIoHandler().addSentMessageHandler(LatestMessageResponse.class, MessageHandler.NOOP);

        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }

    public void run() throws IOException, InterruptedException {
        // Try the first 99, catching the exceptions
        for (int i = 0; i < 100; i++) {
            try {
                bind(new InetSocketAddress(port));
                log.info("Client Producing Server bound on port " + port);
                break;
            } catch (IOException e) {
                // If we are here, and this one also fails, throw the exception
                if (i == 99) {
                    log.info("Failed to bind on port range, quitting");
                    throw e;
                }
                log.info("Failed to bind on port " + port + ", attempting +1");
                port++;
            }
        }

    }

    private void awaitConnections() throws InterruptedException {
        while (getService().getManagedSessionCount() == 0) {
            log.info("No clients connected");
            log.info("R: " + getService().getStatistics().getReadBytesThroughput() +
                    ", W: " + getService().getStatistics().getWrittenBytesThroughput());
            Thread.sleep(3000);
        }

        while (getService().getManagedSessionCount() > 0) {
            log.info("One or more clients connected");
            log.info("R: " + getService().getStatistics().getReadBytesThroughput() +
                    ", W: " + getService().getStatistics().getWrittenBytesThroughput());
            Thread.sleep(3000);
        }

        log.info("No clients connected, cleaning up this test server. Bye!");
    }

}


