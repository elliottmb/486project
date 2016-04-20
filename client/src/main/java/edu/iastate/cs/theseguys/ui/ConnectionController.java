package edu.iastate.cs.theseguys.ui;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.SpringFXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

@Component
public class ConnectionController {

	@FXML
	private Button retry;

	@Autowired
	private SpringFXMLLoader springFXMLLoader;
	@Autowired
	private Client client;

	protected void showButton() {
		retry.setOpacity(1);
		retry.setDisable(false);
	}

	@FXML
	protected void reconnect(ActionEvent event) throws IOException {

	}

}
