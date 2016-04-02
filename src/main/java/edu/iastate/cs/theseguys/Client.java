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

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Client implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    @Autowired
    MessageRepository repository;

    public static void main(String[] args) {
        System.out.println("I touch myself");

        SpringApplication.run(Client.class, args);
    }

    public boolean someLibraryMethod() {

        return true;
    }

    @Override
    public void run(String... args) throws Exception {
        UUID userOne = UUID.randomUUID();
        UUID userTwo = UUID.randomUUID();

        UUID rootId = UUID.randomUUID();

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

    }
}
