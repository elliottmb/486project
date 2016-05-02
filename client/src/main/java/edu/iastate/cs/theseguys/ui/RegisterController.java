package edu.iastate.cs.theseguys.ui;

import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.SpringFXMLLoader;
import edu.iastate.cs.theseguys.eventbus.RegisterEvent;
import edu.iastate.cs.theseguys.eventbus.UserSessionEvent;
import edu.iastate.cs.theseguys.network.RegisterRequest;
import javafx.application.Platform;
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
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RegisterController implements ApplicationListener<UserSessionEvent> {

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

    /**
     * Trys to register given user with given password. Sets error text if passwords do not match.
     * @param event
     * @throws IOException
     */
    @FXML
    protected void register(ActionEvent event) throws IOException {
        if (password.getText().equals(passwordCheck.getText())) {
            client.getAuthorityManager().write(new RegisterRequest(username.getText(), password.getText()));
        } else {
            error.setText("Passwords do not match");
        }
    }

    /**
     * Cancels the registeration and goes back to login.
     * @param event
     * @throws IOException
     */
    @FXML
    protected void cancel(ActionEvent event) throws IOException {
        changeScreens("/fxml/login.fxml");
    }

    /**
     * Changes javafx screens to the given screen.
     * @param screen
     * @throws IOException
     */
    private void changeScreens(String screen) throws IOException {
        Stage stage = (Stage) username.getScene().getWindow();
        Parent root = springFXMLLoader.load(screen);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Event Listener which controls what happens when it receives different types of events from the others.
     */
    @Override
    public void onApplicationEvent(UserSessionEvent event) {
        if (event instanceof RegisterEvent) {
            final RegisterEvent registerEvent = (RegisterEvent) event;
            Platform.runLater(
                    () -> {
                        if (registerEvent.isSuccessful()) {

                            try {
                                changeScreens("/fxml/login.fxml");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            error.setText(registerEvent.getMessage());
                        }
                    }
            );
        }
    }
}
