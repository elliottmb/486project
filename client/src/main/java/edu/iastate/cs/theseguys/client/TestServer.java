package edu.iastate.cs.theseguys.client;

import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import edu.iastate.cs.theseguys.network.LatestMessageRequestHandler;
import edu.iastate.cs.theseguys.network.LoggingMessageHandler;
import edu.iastate.cs.theseguys.network.NewMessageAnnouncement;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;


/**
 * Localhost server to test networking functionality
 *
 */
public class TestServer {
    private static final Logger log = LoggerFactory.getLogger(TestServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        log.info("I touch others");


        IoAcceptor acceptor = new NioSocketAcceptor();
        DemuxingIoHandler demuxIoHandler = new DemuxingIoHandler();

        demuxIoHandler.addReceivedMessageHandler(LatestMessageRequest.class, new LatestMessageRequestHandler());
        demuxIoHandler.addReceivedMessageHandler(NewMessageAnnouncement.class, new LoggingMessageHandler());

        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        acceptor.setHandler(demuxIoHandler);
        acceptor.bind(new InetSocketAddress(5050));  //start accepting connections with this call

        while (acceptor.getManagedSessionCount() == 0) {
            log.info("No clients connected");
            log.info("R: " + acceptor.getStatistics().getReadBytesThroughput() +
                    ", W: " + acceptor.getStatistics().getWrittenBytesThroughput());
            Thread.sleep(3000);
        }

        while (acceptor.getManagedSessionCount() > 0) {
            log.info("One or more clients connected");
            log.info("R: " + acceptor.getStatistics().getReadBytesThroughput() +
                    ", W: " + acceptor.getStatistics().getWrittenBytesThroughput());
            Thread.sleep(3000);
        }

        log.info("No clients connected, cleaning up this test server. Bye!");

        acceptor.dispose();
    }


}
