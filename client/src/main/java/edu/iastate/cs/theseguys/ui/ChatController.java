package edu.iastate.cs.theseguys.ui;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.SpringFXMLLoader;
import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.eventbus.MessageEvent;
import edu.iastate.cs.theseguys.eventbus.NewMessageEvent;
import edu.iastate.cs.theseguys.network.MessageDatagram;
import edu.iastate.cs.theseguys.network.VerificationRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Pair;

@Component
public class ChatController implements ApplicationListener<MessageEvent> {

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
	protected void initialize() {
		// TODO Show users, Get past 20 messages
		submit.setDefaultButton(true);
		for (MessageRecord mr : client.getDatabaseManager().getRepository().findAll()) {
			chat.appendText(mr.getMessageBody() + "\n");
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

	@Override
	public void onApplicationEvent(MessageEvent event) {
		if (event instanceof NewMessageEvent) {

			final NewMessageEvent message = (NewMessageEvent) event;
			Platform.runLater(() -> {
				chat.appendText(message.getMessage().getMessageBody() + "\n");
			});
		}

	}

}
