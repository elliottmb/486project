package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.hibernate.Message;
import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import edu.iastate.cs.theseguys.network.LoggingMessageHandler;
import edu.iastate.cs.theseguys.network.NewMessageAnnouncement;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class DistributedClientManager extends AbstractIoServiceManager<IoConnector> {
    public DistributedClientManager() {
        super(new NioSocketConnector(), new DemuxingIoHandler());

        getDemuxingIoHandler().addSentMessageHandler(LatestMessageRequest.class, new LoggingMessageHandler());
        getDemuxingIoHandler().addSentMessageHandler(NewMessageAnnouncement.class, new LoggingMessageHandler());

        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }

    // TODO: This is temporary, just a test
    public void connectionTest(Message messageA, Message messageB, Message messageC) throws InterruptedException {
        IoSession session;
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

        session.write(new LatestMessageRequest());
        session.write(new NewMessageAnnouncement(messageA));
        session.write(new NewMessageAnnouncement(messageB));
        session.write(new NewMessageAnnouncement(messageC));

        // wait until the summation is done
        //session.getCloseFuture().awaitUninterruptibly();

        session.closeOnFlush().awaitUninterruptibly();
    }

}
