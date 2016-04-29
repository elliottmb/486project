package edu.iastate.cs.theseguys.ui;

import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.SpringFXMLLoader;
import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.eventbus.LogoutEvent;
import edu.iastate.cs.theseguys.eventbus.MessageEvent;
import edu.iastate.cs.theseguys.eventbus.NewMessageEvent;
import edu.iastate.cs.theseguys.network.LogoutRequest;
import edu.iastate.cs.theseguys.network.MessageDatagram;
import edu.iastate.cs.theseguys.network.VerificationRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Component
public class ChatController implements ApplicationListener<ApplicationEvent> {

    @FXML
    private TextArea chat;

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

    @FXML
    protected void initialize() {
        // TODO Show users, Get past 20 messages
        submit.setDefaultButton(true);
        List<MessageRecord> messages = client.getDatabaseManager().getRepository().findFirst20ByOrderByTimestampDesc();
        for (int i = messages.size() - 1; i >= 0; i--) {
            MessageRecord m = messages.get(i);
            chat.appendText(m.getUserId() + ": " + m.getMessageBody() + "\n");
        }
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
    protected void logout(ActionEvent event){
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
                        chat.appendText(newMessageEvent.getMessage().getUserId() + ": " + newMessageEvent.getMessage().getMessageBody() + "\n");
                    }
            );
        }
        
        if(event instanceof LogoutEvent){
        	final LogoutEvent logoutEvent = (LogoutEvent) event;
        	if(logoutEvent.isConfirmed()){
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
