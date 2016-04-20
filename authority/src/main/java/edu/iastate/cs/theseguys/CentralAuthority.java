package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.security.AuthoritySecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


/**
 * The CentralAuthority will be responsible for validating Messages
 * from a Client before it can send that MessageRecord to other Clients.
 * Additionally, maintains connections to all currently operating
 * Clients, so that new Clients know where to connect to in order to
 * build up their peer network.
 */

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class CentralAuthority implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CentralAuthority.class);

    @Autowired
    private AuthorityClientManager clientManager;


    public static void main(String[] args) throws IOException {
        System.out.println("I touch others");

        SpringApplication.run(CentralAuthority.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("RUNNING");
        AuthoritySecurity authSec = new AuthoritySecurity();
        log.info("Loading or generating key pair");
        if (authSec.generateKeyPair()) {
            log.warn("Generated new key pair");
        } else {
            log.info("Successfully loaded key pair from file");
        }

        clientManager.run();
    }

}
