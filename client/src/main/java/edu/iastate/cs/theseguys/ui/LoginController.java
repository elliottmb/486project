package edu.iastate.cs.theseguys.ui;

import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.SpringFXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

//TODO For chat use TextArea

@Component
public class LoginController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Text error;

    @Autowired
    private SpringFXMLLoader springFXMLLoader;
    @Autowired
    private Client client;

    @FXML
    protected void button(ActionEvent event) throws IOException {
        //TODO add signin checking here. If failed, show error in text error
        if (event.getSource() instanceof Button) {
            Button pressed = (Button) event.getSource();
            Stage stage = (Stage) pressed.getScene().getWindow();
            Parent root = null;
            if (pressed.getText().equals("Register")) {
                root = springFXMLLoader.load("/fxml/register.fxml");
            } else {
                root = springFXMLLoader.load("/fxml/chat.fxml");
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

}
