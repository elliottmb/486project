package edu.iastate.cs.theseguys.distributed;

import edu.iastate.cs.theseguys.AbstractIoServiceManager;
import edu.iastate.cs.theseguys.hibernate.Message;
import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import edu.iastate.cs.theseguys.network.LoggingMessageHandler;
import edu.iastate.cs.theseguys.network.NewMessageAnnouncement;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class ClientManager extends AbstractIoServiceManager<IoConnector> {
    private static final Logger log = LoggerFactory.getLogger(ClientManager.class);

    public ClientManager() {
        super(new NioSocketConnector(), new ClientDemuxingIoHandler());

        getDemuxingIoHandler().addSentMessageHandler(LatestMessageRequest.class, new LoggingMessageHandler());
        getDemuxingIoHandler().addSentMessageHandler(NewMessageAnnouncement.class, new LoggingMessageHandler());

        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }

    /**
     * This function attempts to connect to the given address. This method is non-blocking and returns the base
     * ConnectFuture.
     *
     * @param host Server to connect to
     * @return ConnectFuture
     */
    public ConnectFuture connect(InetSocketAddress host) {
        log.info("Attempting to connect to " + host.getHostName() + ":" + host.getPort());

        return getService().connect(host);
    }

    // TODO: This is temporary, just a test
    public void connectionTest(IoSession session, Message messageA, Message messageB, Message messageC) {
       /* IoSession session;
        while (true) {
            try {
                ConnectFuture future = getService().connect(new InetSocketAddress("localhost", 5050));
                future.awaitUninterruptibly();
                session = future.getSession();
                break;
            } catch (RuntimeIoException e) {
                System.err.println("Failed to connect.");
                e.printStackTrace();
                Thread.sleep(5000);
            }
        }
*/
        session.write(new LatestMessageRequest());
        session.write(new NewMessageAnnouncement(messageA));
        session.write(new NewMessageAnnouncement(messageB));
        session.write(new NewMessageAnnouncement(messageC));

        // wait until the summation is done
        //session.getCloseFuture().awaitUninterruptibly();

        session.closeOnFlush().awaitUninterruptibly();
    }

}
