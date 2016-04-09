package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.database.MessageRepository;
import edu.iastate.cs.theseguys.hibernate.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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

        distributedServerManager.run();

        // Just a test... woo
        distributedClientManager.connectionTest(messageA, messageB, messageC);
        distributedClientManager.connectionTest(messageA, messageB, messageC);

        distributedClientManager.dispose();
        distributedServerManager.dispose();
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
