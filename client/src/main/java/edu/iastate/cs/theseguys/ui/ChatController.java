package edu.iastate.cs.theseguys.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {

	@FXML
	private TextArea chat;

	@FXML
	private TextArea users;

	@FXML
	private TextField input;

	@FXML
	private Button submit;

	@FXML
	protected void submitText(ActionEvent event) {
		if (!input.getText().equals("")) {
			chat.appendText(input.getText() + "\n");
		}
	}

}
