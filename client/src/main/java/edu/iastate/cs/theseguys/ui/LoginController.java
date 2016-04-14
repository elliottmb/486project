package edu.iastate.cs.theseguys.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

//TODO For chat use TextArea

public class LoginController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Text error;

    @FXML
    protected void button(ActionEvent event) throws IOException {
        //TODO add signin checking here. If failed, show error in text error
    	if(event.getSource() instanceof Button){
    		Button pressed = (Button) event.getSource();
    		Stage stage = (Stage) pressed.getScene().getWindow();
    		Parent root = null;
    		if (pressed.getText().equals("Register")){
    			root = FXMLLoader.load(getClass().getResource("register.fxml"));
    		}
    		else{
    			root = FXMLLoader.load(getClass().getResource("chat.fxml"));
    		}
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    }

}
