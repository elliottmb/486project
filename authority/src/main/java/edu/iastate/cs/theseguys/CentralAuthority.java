package edu.iastate.cs.theseguys;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



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
	private static AuthorityClientManager clientManager;
	
	
	
    public static void main(String[] args) throws IOException {
        System.out.println("I touch others");
        
        SpringApplication.run(CentralAuthority.class, args);
    }
    
    
    @Override
    public void run(String... args) throws Exception {
    	System.out.println("RUNNING");
    }

}
