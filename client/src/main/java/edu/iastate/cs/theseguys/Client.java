package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.database.MessageRepository;
import edu.iastate.cs.theseguys.distributed.ClientManager;
import edu.iastate.cs.theseguys.distributed.ServerManager;
import org.apache.mina.core.future.ConnectFuture;
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

/**
 * Client that runs on a user's machine.  Maintains a database
 * of messages, as well as a list of other Clients that this Client
 * is connected to, and a list of Clients which are connected to
 * this Client.
 * Also maintains a connection to the Central Authority.
 * Connected Clients with exchange messages with each other using
 * the different subclasses of the AbstractMessage class.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Client implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    @Autowired
    private AuthorityManager authorityManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private ServerManager serverManager;
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

        MessageRecord root = new MessageRecord(rootId, UUID.randomUUID(), "Welcome to DILC", new Timestamp(System.currentTimeMillis()), new byte[256]);
        root.setFather(root);
        root.setMother(root);
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        MessageRecord messageA = new MessageRecord(UUID.randomUUID(), userOne, "test", new Timestamp(System.currentTimeMillis()), new byte[256]);
        messageA.setFather(root);
        messageA.setMother(root);
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        MessageRecord messageB = new MessageRecord(UUID.randomUUID(), userTwo, "test 2", new Timestamp(System.currentTimeMillis()), new byte[256]);
        messageB.setFather(root);
        messageB.setMother(messageA);
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        MessageRecord messageC = new MessageRecord(UUID.randomUUID(), userOne, "test 3", new Timestamp(System.currentTimeMillis()), new byte[256]);
        messageC.setFather(messageA);
        messageC.setMother(messageB);

        repository.save(root);
        repository.save(messageA);
        repository.save(messageB);
        repository.save(messageC);

        //log.info("Messages currently in database: ");
        for (MessageRecord message : repository.findAll()) {
            //log.info(message.toString());
        }

        // log.info("Messages currently in database for user '" + userOne + "':");
        for (MessageRecord message : repository.findByUserId(userOne)) {
            //log.info(message.toString());
        }

        serverManager.run();

        ConnectFuture firstConnection = clientManager.connect(new InetSocketAddress("localhost", 5050));
        //ConnectFuture secondConnection = clientManager.connect(new InetSocketAddress("localhost", 5050));


        // We need to wait on at least one connection since we're doing it in the main method here instead of via a handler
        firstConnection.awaitUninterruptibly();
        // secondConnection.awaitUninterruptibly();

        //clientManager.write(new NewMessageAnnouncement(messageA));
        //clientManager.write(new NewMessageAnnouncement(messageB));
        //clientManager.write(new NewMessageAnnouncement(messageC));
        //clientManager.write(new LatestMessageRequest());

        MessageRecord oldest = databaseManager.getRepository().findFirstByOrderByTimestampAsc();

        log.info("---- Oldest ----");
        log.info("Self: " + oldest.toString());
        log.info("Father: " + oldest.getFather().toString());
        log.info("Mother: " + oldest.getMother().toString());
        log.info("Left Children: " + oldest.getLeftChildren().toString());
        log.info("Right Children: " + oldest.getRightChildren().toString());


        MessageRecord youngest = databaseManager.getRepository().findFirstByOrderByTimestampDesc();

        log.info("---- Youngest ----");
        log.info("Self: " + youngest.toString());
        log.info("Father: " + youngest.getFather().toString());
        log.info("Mother: " + youngest.getMother().toString());
        log.info("Left Children: " + youngest.getLeftChildren().toString());
        log.info("Right Children: " + youngest.getRightChildren().toString());

        clientManager.dispose();
        serverManager.dispose();
    }

    public AuthorityManager getAuthorityManager() {
        return authorityManager;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
