package edu.iastate.cs.theseguys.ui;

import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.SpringFXMLLoader;
import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.eventbus.LogoutEvent;
import edu.iastate.cs.theseguys.eventbus.NewMessageEvent;
import edu.iastate.cs.theseguys.network.LogoutRequest;
import edu.iastate.cs.theseguys.network.MessageDatagram;
import edu.iastate.cs.theseguys.network.UserListRequest;
import edu.iastate.cs.theseguys.network.VerificationRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ChatController implements ApplicationListener<ApplicationEvent> {

    @FXML
    private ListView<MessageDatagram> chat;

    @FXML
    private TextArea users;

    @FXML
    private TextField input;

    @FXML
    private Button submit;

    @FXML
    private Button logout;
    @Autowired
    private SpringFXMLLoader springFXMLLoader;
    @Autowired
    private Client client;

    private ObservableList<MessageDatagram> chatMessages;

    private SortedList<MessageDatagram> sortedList;

    public ChatController() {
        chatMessages = FXCollections.observableArrayList();
        sortedList = new SortedList<>(chatMessages);
        sortedList.setComparator(
                (o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp())
        );
    }

    @FXML
    protected void initialize() {
        chat.setCellFactory(
                new Callback<ListView<MessageDatagram>, ListCell<MessageDatagram>>() {

                    @Override
                    public ListCell<MessageDatagram> call(ListView<MessageDatagram> p) {

                        return new ListCell<MessageDatagram>() {
                            @Override
                            protected void updateItem(MessageDatagram messageDatagram, boolean bln) {
                                super.updateItem(messageDatagram, bln);
                                if (messageDatagram != null) {
                                    Map<UUID, String> knownUsernames = client.getDatabaseManager().getKnownUsernames();

                                    String time = messageDatagram.getTimestamp().toString();
                                    time = time.substring(0, time.indexOf("."));

                                    if (knownUsernames != null && knownUsernames.size() > 0) {
                                        String username = knownUsernames.get(messageDatagram.getUserId());
                                        if (username != null) {
                                            setText("<" + username + "|" + time + ">: " + messageDatagram.getMessageBody());
                                            return;
                                        } else {
                                            // Since we didn't have it, let's look it up for later... TODO: Update chat list on response?
                                            client.getAuthorityManager().write(new UserListRequest());
                                        }
                                    }
                                    setText("<u#" + messageDatagram.getUserId().toString().substring(0, 7) + "|" + time + ">: " + messageDatagram.getMessageBody());
                                }
                            }
                        };
                    }
                }
        );
        chat.setItems(sortedList);
        List<MessageRecord> messages = client.getDatabaseManager()
                .getRepository()
                .findFirst20ByOrderByTimestampDesc();
        messages
                .stream()
                .map(MessageRecord::toDatagram)
                .collect(Collectors.toCollection(() -> chatMessages));
        submit.setDefaultButton(true);

        // TODO Show users
    }

    @FXML
    protected void submitText(ActionEvent event) {
        if (!input.getText().equals("")) {
            Pair<MessageRecord, MessageRecord> parents = client.getDatabaseManager().getIdealParentRecords();
            MessageDatagram messageDatagram = new MessageDatagram(UUID.randomUUID(),
                    client.getAuthorityManager().getUserId(), parents.getKey().getId(), parents.getValue().getId(),
                    input.getText(), new Timestamp(System.currentTimeMillis()), new byte[256]);

            client.getAuthorityManager().write(new VerificationRequest(messageDatagram));
        }
        input.setText("");
    }

    @FXML
    protected void logout(ActionEvent event) {
        client.getAuthorityManager().write(new LogoutRequest());
    }

    private void changeScreens(String screen) throws IOException {
        Stage stage = (Stage) chat.getScene().getWindow();
        Parent root = springFXMLLoader.load(screen);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof NewMessageEvent) {

            final NewMessageEvent newMessageEvent = (NewMessageEvent) event;
            Platform.runLater(
                    () -> {
                        chatMessages.add(newMessageEvent.getMessage().toDatagram());
                    }
            );
        }

        if (event instanceof LogoutEvent) {
            final LogoutEvent logoutEvent = (LogoutEvent) event;
            if (logoutEvent.isConfirmed()) {
                Platform.runLater(
                        () -> {
                            try {
                                changeScreens("/fxml/login.fxml");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
        }

    }

}
