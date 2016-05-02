package edu.iastate.cs.theseguys.ui;

import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.SpringFXMLLoader;
import edu.iastate.cs.theseguys.eventbus.AuthorityConnectedEvent;
import edu.iastate.cs.theseguys.eventbus.AuthorityConnectionFailedEvent;
import edu.iastate.cs.theseguys.eventbus.AuthorityEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConnectionController implements ApplicationListener<AuthorityEvent> {

    @FXML
    private Button retry;
    @FXML
    private ProgressIndicator progressIndicator;

    @Autowired
    private SpringFXMLLoader springFXMLLoader;
    @Autowired
    private Client client;

    /**
     * Shows the retry button.
     */
    protected void showButton() {
        retry.setOpacity(1);
        retry.setDisable(false);
    }

    /**
     * Method to try to reconnect.
     * @param event
     * @throws IOException
     */
    @FXML
    protected void reconnect(ActionEvent event) throws IOException {

    }

    /**
     * Event Listener which controls what happens when it receives different types of events from the others.
     */
    @Override
    public void onApplicationEvent(AuthorityEvent event) {
        if (event instanceof AuthorityConnectedEvent) {
            AuthorityConnectedEvent loginEvent = (AuthorityConnectedEvent) event;

            final Stage stage = (Stage) retry.getScene().getWindow();
            Platform.runLater(
                    () -> {
                        try {
                            Parent root = springFXMLLoader.load("/fxml/login.fxml");
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } else {
            if (event instanceof AuthorityConnectionFailedEvent) {
            	retry.setDisable(false);
            	retry.setOpacity(1.0);
                AuthorityConnectionFailedEvent authorityConnectionFailedEvent = (AuthorityConnectionFailedEvent) event;

            }
        }
    }
}
