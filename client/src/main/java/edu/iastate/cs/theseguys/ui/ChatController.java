package edu.iastate.cs.theseguys.ui;

import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.SpringFXMLLoader;
import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.network.MessageDatagram;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class ChatController {

    @FXML
    private TextArea chat;

    @FXML
    private TextArea users;

    @FXML
    private TextField input;

    @FXML
    private Button submit;
    @Autowired
    private SpringFXMLLoader springFXMLLoader;
    @Autowired
    private Client client;


    @FXML
    protected void submitText(ActionEvent event) {
        if (!input.getText().equals("")) {
            chat.appendText(input.getText() + "\n");
            Pair<MessageRecord, MessageRecord> parents = client.getDatabaseManager().getIdealParentRecords();
            client.getDatabaseManager().getReady().push(new Pair<>(-1L, new MessageDatagram(UUID.randomUUID(), UUID.randomUUID(), parents.getKey().getId(), parents.getValue().getId(), input.getText(), new Timestamp(System.currentTimeMillis()), new byte[256])));
        }
    }

}
