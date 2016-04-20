package edu.iastate.cs.theseguys.ui;

import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.SpringFXMLLoader;
import edu.iastate.cs.theseguys.network.RegisterRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RegisterController {

	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private PasswordField passwordCheck;
	@FXML
	private Button register;
	@FXML
	private Button cancel;
	@FXML
	private Label error;

	@Autowired
	private SpringFXMLLoader springFXMLLoader;
	@Autowired
	private Client client;

	@FXML
	protected void register(ActionEvent event) throws IOException {
		if (password.getText().equals(passwordCheck.getText())) {
			client.getAuthorityManager().write(new RegisterRequest(username.getText(), password.getText()));
			changeScreens("/fxml/login.fxml");
		} else {
			error.setText("Passwords do not match");
		}
	}

	@FXML
	protected void cancel(ActionEvent event) throws IOException {
		changeScreens("/fxml/login.fxml");
	}

	private void changeScreens(String screen) throws IOException {
		Stage stage = (Stage) username.getScene().getWindow();
		Parent root = springFXMLLoader.load(screen);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
