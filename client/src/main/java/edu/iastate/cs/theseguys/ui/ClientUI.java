package edu.iastate.cs.theseguys.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Need Login, register and chat screens

public class ClientUI extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
		stage.setTitle("TEST");
		stage.setScene(new Scene(root, 400, 375));
		stage.show();
		
	}
	
	public static void main(String[] args) {
		Application.launch(ClientUI.class, args);
	}

}
