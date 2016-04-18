package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.database.MessageRepository;
import edu.iastate.cs.theseguys.distributed.ClientManager;
import edu.iastate.cs.theseguys.distributed.ServerManager;
import edu.iastate.cs.theseguys.network.MessageDatagram;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.mina.core.future.ConnectFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.awt.*;
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
        SwingUtilities.invokeLater(() -> {
            JFXPanel jfxPanel = new JFXPanel(); // initializes JavaFX environment
            jfxPanel.setSize(800, 600);
            jfxPanel.setVisible(true);
            latch.countDown();
        });
        latch.await();

        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("TEST");
            stage.setOnCloseRequest(
                    event -> {
                        dispose();
                        Platform.exit();
                        System.exit(0);
                    }
            );

            Parent root = null;
            try {
                root = springFXMLLoader.load("/fxml/login.fxml");
                stage.setScene(new Scene(root, 800, 600));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
            stage.show();

            //Application.launch(ClientUI.class, args);
        });

        ConnectFuture authorityFuture = authorityManager.connect(new InetSocketAddress(9090));

        authorityFuture.awaitUninterruptibly();

        // TODO: Actually check for connectivity and handle retrying.
        /*
        EventQueue.invokeLater(
                () -> {
                    JFrame frame = new SimpleFrame(this);


                }
        );*/


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

        youngest = databaseManager.getLatestMessage();

        log.info("---- Youngest (getLatestMessage) ----");
        log.info("Self: " + youngest.toString());
        log.info("Father: " + youngest.getFather().toString());
        log.info("Mother: " + youngest.getMother().toString());
        log.info("Left Children: " + youngest.getLeftChildren().toString());
        log.info("Right Children: " + youngest.getRightChildren().toString());

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("> ");
        String s;
        Pair<MessageRecord, MessageRecord> idealParentRecords;
        while ((s = in.readLine()) != null) {
            if (":q".equalsIgnoreCase(s))
                break;

            if (s.toLowerCase().startsWith(":c")) {
                String[] arguments = s.split(" ");
                if (arguments.length == 3) {
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(arguments[1], Integer.parseInt(arguments[2]));
                    clientManager.connect(inetSocketAddress);
                } else {
                    System.out.println("Expecting two arguments, got " + (arguments.length - 1));
                }
                continue;
            } else {
                if (":l".equalsIgnoreCase(s)) {
                    System.out.println(clientManager.getService().getManagedSessions());
                } else {

                    idealParentRecords = databaseManager.getIdealParentRecords();

                    System.out.println("We are only locally handling this, for now");
                    MessageDatagram test = new MessageDatagram(
                            UUID.randomUUID(),
                            userOne,
                            idealParentRecords.getKey().getId(),
                            idealParentRecords.getValue().getId(),
                            s,
                            new Timestamp(System.currentTimeMillis()),
                            new byte[256]
                    );

                    databaseManager.getWaiting().push(new Pair<>(-1L, test));
                }
            }
            System.out.print("> ");
        }

        Pair<MessageRecord, MessageRecord> possibleParents = databaseManager.getIdealParentRecords();

        log.info("---- Possible parents for a new message are:");
        log.info(possibleParents.getKey().toString());
        log.info(possibleParents.getKey().getFather().toString());
        log.info(possibleParents.getKey().getMother().toString());
        log.info(possibleParents.getValue().toString());
        log.info(possibleParents.getValue().getFather().toString());
        log.info(possibleParents.getValue().getMother().toString());


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

    class SimpleFrame extends JFrame {
        JPanel pnlButton = new JPanel();
        // Buttons
        JButton btnAddFlight = new JButton("Add Flight");
        private Client client;

        public SimpleFrame(Client client) {
            super("dILC");

            this.client = client;
            setSize(new Dimension(300, 300));
            setPreferredSize(new Dimension(300, 300));
            setMaximumSize(new Dimension(300, 300));
            setMinimumSize(new Dimension(300, 300));
            setVisible(true);
            pack();
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            btnAddFlight.setBounds(60, 400, 220, 30);

            // JPanel bounds
            pnlButton.setBounds(800, 800, 200, 100);

            // Adding to JFrame
            pnlButton.add(btnAddFlight);
            add(pnlButton);

            btnAddFlight.addActionListener(e -> {
                Pair<MessageRecord, MessageRecord> parents = client.databaseManager.getIdealParentRecords();

                MessageDatagram test = new MessageDatagram(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        parents.getKey().getId(),
                        parents.getValue().getId(),
                        "From a button!",
                        new Timestamp(System.currentTimeMillis()),
                        new byte[256]
                );

                databaseManager.getWaiting().push(new Pair<>(123912039L, test));

            });

        }
    }
}
