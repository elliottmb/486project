package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.database.MessageRepository;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.UUID;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Client implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    @Autowired
    private AuthorityClientManager authorityClientManager;
    @Autowired
    private DistributedClientManager distributedClientManager;
    @Autowired
    private DistributedServerManager distributedServerManager;
    @Autowired
    private DatabaseManager databaseManager;


    public static void main(String[] args) {
        log.info("I touch myself");

        SpringApplication.run(Client.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        UUID userOne = UUID.randomUUID();
        UUID userTwo = UUID.randomUUID();

        UUID rootId = UUID.randomUUID();

        MessageRepository repository = this.getDatabaseManager().getRepository();

        Message root = new Message(rootId, UUID.randomUUID(), rootId, rootId, "Welcome to DILC", new Timestamp(System.currentTimeMillis()), new byte[256]);
        Message messageA = new Message(UUID.randomUUID(), userOne, root.getId(), root.getId(), "test", new Timestamp(System.currentTimeMillis()), new byte[256]);
        Message messageB = new Message(UUID.randomUUID(), userTwo, root.getId(), messageA.getId(), "test 2", new Timestamp(System.currentTimeMillis()), new byte[256]);
        Message messageC = new Message(UUID.randomUUID(), userOne, messageA.getId(), messageB.getId(), "test 3", new Timestamp(System.currentTimeMillis()), new byte[256]);

        repository.save(root);
        repository.save(messageA);
        repository.save(messageB);
        repository.save(messageC);

        log.info("Messages currently in database: ");
        for (Message message : repository.findAll()) {
            log.info(message.toString());
        }

        log.info("Messages currently in database for user '" + userOne + "':");
        for (Message message : repository.findByUserId(userOne)) {
            log.info(message.toString());
        }

        IoConnector connector = new NioSocketConnector();
        DemuxingIoHandler demuxIoHandler = new DemuxingIoHandler();


        demuxIoHandler.addSentMessageHandler(LatestMessageRequest.class, new LoggingMessageHandler());
        demuxIoHandler.addSentMessageHandler(NewMessageAnnouncement.class, new LoggingMessageHandler());

        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        connector.setHandler(demuxIoHandler);

        IoSession session;
        while (true) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress("localhost", 5050));
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
        session.getCloseFuture().awaitUninterruptibly();

        connector.dispose();
    }

    public AuthorityClientManager getAuthorityClientManager() {
        return authorityClientManager;
    }

    public DistributedClientManager getDistributedClientManager() {
        return distributedClientManager;
    }

    public DistributedServerManager getDistributedServerManager() {
        return distributedServerManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
