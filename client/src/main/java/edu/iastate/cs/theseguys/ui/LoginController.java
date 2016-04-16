package edu.iastate.cs.theseguys.ui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//TODO For chat use TextArea

public class LoginController {

	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Text error;

	@FXML
	protected void signin(ActionEvent event) {
		//TODO add signin checking here. If failed, show error in text error
		error.setText("TODO");
		System.out.println("User: " + username);
	}
	
	@FXML
	protected void register(ActionEvent event) throws IOException {
		//TODO add register logic here.
		Stage stage = (Stage) username.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
