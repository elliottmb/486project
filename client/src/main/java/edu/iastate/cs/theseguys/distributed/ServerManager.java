package edu.iastate.cs.theseguys.distributed;

import edu.iastate.cs.theseguys.AbstractIoServiceManager;
import edu.iastate.cs.theseguys.client.TestServer;
import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import edu.iastate.cs.theseguys.network.LatestMessageRequestHandler;
import edu.iastate.cs.theseguys.network.LoggingMessageHandler;
import edu.iastate.cs.theseguys.network.NewMessageAnnouncement;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;

@Component
public class ServerManager extends AbstractIoServiceManager<IoAcceptor> {
    private static final Logger log = LoggerFactory.getLogger(TestServer.class);
    private static int port;

    public ServerManager() {
        super(new NioSocketAcceptor(), new ServerDemuxingIoHandler());
        port = 5050;
    }

    public void run() throws IOException, InterruptedException {
        prepareAcceptor(port);
        //awaitConnections();

        //acceptor.dispose();
    }

    private void prepareAcceptor(int port) throws IOException {
        getDemuxingIoHandler().addReceivedMessageHandler(LatestMessageRequest.class, new LatestMessageRequestHandler());
        getDemuxingIoHandler().addReceivedMessageHandler(NewMessageAnnouncement.class, new LoggingMessageHandler());

        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        getService().bind(new InetSocketAddress(port));
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


