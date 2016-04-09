package edu.iastate.cs.theseguys;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import edu.iastate.cs.theseguys.client.TestServer;
import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import edu.iastate.cs.theseguys.network.LatestMessageRequestHandler;
import edu.iastate.cs.theseguys.network.LoggingMessageHandler;
import edu.iastate.cs.theseguys.network.NewMessageAnnouncement;

@Component
public class DistributedServerManager {
    private static final Logger log = LoggerFactory.getLogger(TestServer.class);
    private static IoAcceptor acceptor;
    private static DemuxingIoHandler demuxIoHandler;
    private static int port;
    
    public DistributedServerManager() {
    	acceptor = new NioSocketAcceptor();
    	demuxIoHandler = new DemuxingIoHandler();
    	port = 5050;
    }
    
    
    public void run() throws IOException, InterruptedException {
		prepareAcceptor(port);
		awaitConnections();
	
	    acceptor.dispose();
    }
    
    private static void prepareAcceptor(int port) throws IOException {
	    demuxIoHandler.addReceivedMessageHandler(LatestMessageRequest.class, new LatestMessageRequestHandler());
	    demuxIoHandler.addReceivedMessageHandler(NewMessageAnnouncement.class, new LoggingMessageHandler());
	
	    acceptor.getFilterChain().addLast("logger", new LoggingFilter());
	    acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
	    acceptor.setHandler(demuxIoHandler);
	    acceptor.bind(new InetSocketAddress(port));
    }
    
    private static void awaitConnections() throws InterruptedException {
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
    }

}
	

