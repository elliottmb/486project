package edu.iastate.cs.theseguys.ui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
	
	@FXML
	protected void register(ActionEvent event){
		error.setText("TODO");
	}
	
	@FXML
	protected void cancel(ActionEvent event) throws IOException{
		Stage stage = (Stage) username.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
