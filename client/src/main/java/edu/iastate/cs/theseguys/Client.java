package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.database.MessageRepository;
import edu.iastate.cs.theseguys.distributed.ClientManager;
import edu.iastate.cs.theseguys.distributed.ServerManager;
import edu.iastate.cs.theseguys.eventbus.AuthorityConnectedEvent;
import edu.iastate.cs.theseguys.eventbus.AuthorityConnectionFailedEvent;
import edu.iastate.cs.theseguys.network.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

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
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private AuthorityManager authorityManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private ServerManager serverManager;
    @Autowired
    private DatabaseManager databaseManager;
    @Autowired
    private SpringFXMLLoader springFXMLLoader;

    public static void main(String[] args) {
        log.info("I touch myself");

        new SpringApplicationBuilder(Client.class)
                .headless(false)
                .web(false)
                .run(args);
    }

    public void dispose() {
        clientManager.dispose();
        serverManager.dispose();
        authorityManager.dispose();
        databaseManager.dispose();
    }

    @Override
    public void run(String... args) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(
                () -> {
                    JFXPanel jfxPanel = new JFXPanel(); // initializes JavaFX environment
                    jfxPanel.setSize(800, 600);
                    jfxPanel.setVisible(true);
                    latch.countDown();
                }
        );
        latch.await();

        final CountDownLatch stageLatch = new CountDownLatch(1);
        Platform.runLater(
                () -> {
                    Stage stage = new Stage();
                    stage.setTitle("Distributed Immutable Ledger Chat");
                    stage.setOnCloseRequest(
                            event -> {
                                dispose();
                                Platform.exit();
                                System.exit(0);
                            }
                    );

                    Parent root = null;
                    try {
                        root = springFXMLLoader.load("/fxml/connection.fxml");
                        stage.setScene(new Scene(root, 800, 600));
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                    stage.show();
                    stageLatch.countDown();
                }
        );
        stageLatch.await();

        ConnectFuture authorityFuture = null;
        for (int i = 0; i < 5; i++) {
            try {

                // Switch the next two lines for local variant.
                //authorityFuture = authorityManager.connect(new InetSocketAddress(9090));
                authorityFuture = authorityManager.connect(new InetSocketAddress("162.243.102.180", 9090));
                authorityFuture.awaitUninterruptibly();
                if (authorityFuture.getSession().isConnected()) {
                    break;
                }
            } catch (RuntimeIoException e) {
                log.warn("Failed to connect to authority, waiting 3 seconds.");
                e.printStackTrace();
                Thread.sleep(3000);
            }
        }
        if (authorityFuture != null && authorityFuture.isConnected()) {
            log.info("Successfully connect to authority.");
            applicationEventPublisher.publishEvent(new AuthorityConnectedEvent(this));
        } else {
            log.warn("Failed to connect to authority, max number of attempts. Exiting.");
            applicationEventPublisher.publishEvent(new AuthorityConnectionFailedEvent(this));
            //dispose();
            //System.exit(0);
        }

        UUID rootId = new UUID(0, 0);

        MessageRepository repository = this.getDatabaseManager().getRepository();

        // Make sure client has "root" node
        if (!repository.exists(rootId)) {
            MessageRecord root = new MessageRecord(rootId, rootId, "Welcome to DILC", new Timestamp(0L), new byte[256]);
            root.setFather(root);
            root.setMother(root);
            repository.save(root);
        }

        serverManager.run();


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

        youngest = databaseManager.getLatestMessage();

        log.info("---- Youngest (getLatestMessage) ----");
        log.info("Self: " + youngest.toString());
        log.info("Father: " + youngest.getFather().toString());
        log.info("Mother: " + youngest.getMother().toString());
        log.info("Left Children: " + youngest.getLeftChildren().toString());
        log.info("Right Children: " + youngest.getRightChildren().toString());

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please login (:l username password) or register (:r username password)");
        System.out.print("> ");
        String s;
        Pair<MessageRecord, MessageRecord> idealParentRecords;
        while ((s = in.readLine()) != null) {
            if (":q".equalsIgnoreCase(s))
                break;

            if (s.toLowerCase().startsWith(":l ")) {
                if (authorityManager.isLoggedIn()) {
                    System.out.println("Please log out first using :loff");
                } else {
                    String[] arguments = s.split(" ");
                    if (arguments.length == 3) {
                        InetSocketAddress serverAddress = (InetSocketAddress) serverManager.getService().getLocalAddress();
                        authorityManager.write(new LoginRequest(arguments[1], arguments[2], serverAddress.getPort()));
                    } else {
                        System.out.println("Expecting two arguments, got " + (arguments.length - 1));
                    }
                }
            } else {
                if (s.toLowerCase().startsWith(":r ")) {
                    if (authorityManager.isLoggedIn()) {
                        System.out.println("Please log out first using :dc");
                    } else {
                        String[] arguments = s.split(" ");
                        if (arguments.length == 3) {
                            authorityManager.write(new RegisterRequest(arguments[1], arguments[2]));
                        } else {
                            System.out.println("Expecting two arguments, got " + (arguments.length - 1));
                        }
                    }
                } else {
                    if (authorityManager.isLoggedIn()) {
                        if (s.toLowerCase().startsWith(":c")) {
                            String[] arguments = s.split(" ");
                            if (arguments.length == 3) {
                                InetSocketAddress inetSocketAddress = new InetSocketAddress(arguments[1], Integer.parseInt(arguments[2]));
                                clientManager.connect(inetSocketAddress);
                            } else {
                                System.out.println("Expecting two arguments, got " + (arguments.length - 1));
                            }
                        } else {
                            if (":lc".equalsIgnoreCase(s)) {
                                System.out.println(clientManager.getService().getManagedSessions());
                                System.out.println(serverManager.getService().getManagedSessions());
                            } else {
                                if (s.toLowerCase().equals(":dc")) {
                                    authorityManager.write(new LogoutRequest());
                                    System.out.println("Logged off...");
                                } else {
                                    idealParentRecords = databaseManager.getIdealParentRecords();

                                    System.out.println("We are sending the message off to the central authority!");
                                    MessageDatagram test = new MessageDatagram(
                                            UUID.randomUUID(),
                                            authorityManager.getUserId(),
                                            idealParentRecords.getKey().getId(),
                                            idealParentRecords.getValue().getId(),
                                            s,
                                            new Timestamp(System.currentTimeMillis()),
                                            new byte[256]
                                    );

                                    authorityManager.write(new VerificationRequest(test));
                                }
                            }
                        }
                    } else {
                        System.out.println("Please login (:l username password) or register (:r username password)");
                    }
                }
            }
            System.out.print("> ");
        }

        Pair<MessageRecord, MessageRecord> possibleParents = databaseManager.getIdealParentRecords();

        log.info("---- Possible parents for a new message are:");
        log.info("Father: " + possibleParents.getKey().toString());
        log.info("-- Father: " + possibleParents.getKey().getFather().toString());
        log.info("-- Mother: " + possibleParents.getKey().getMother().toString());
        log.info("Mother: " + possibleParents.getValue().toString());
        log.info("-- Father: " + possibleParents.getValue().getFather().toString());
        log.info("-- Mother: " + possibleParents.getValue().getMother().toString());


        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        youngest = databaseManager.getLatestMessage();

        log.info("---- Last submitted message ----");
        log.info("Self: " + youngest.toString());
        log.info("Father: " + youngest.getFather().toString());
        log.info("Mother: " + youngest.getMother().toString());
        log.info("Left Children: " + youngest.getLeftChildren().toString());
        log.info("Right Children: " + youngest.getRightChildren().toString());

        dispose();
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
