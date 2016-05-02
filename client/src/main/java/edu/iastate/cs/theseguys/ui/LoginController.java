package edu.iastate.cs.theseguys.ui;

import edu.iastate.cs.theseguys.Client;
import edu.iastate.cs.theseguys.SpringFXMLLoader;
import edu.iastate.cs.theseguys.eventbus.LoginEvent;
import edu.iastate.cs.theseguys.eventbus.UserSessionEvent;
import edu.iastate.cs.theseguys.network.LoginRequest;
import javafx.application.Platform;
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
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;



@Component
public class LoginController implements ApplicationListener<UserSessionEvent> {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Text error;
    @FXML
    private Button login;
    @FXML
    private Button register;
    @Autowired
    private SpringFXMLLoader springFXMLLoader;
    @Autowired
    private Client client;

    /**
     * Controls what happens when a given button is pressed. Controls the login and the register buttons.
     * @param event
     * @throws IOException
     */
    @FXML
    protected void button(ActionEvent event) throws IOException {
        if (event.getSource() instanceof Button) {
            Button pressed = (Button) event.getSource();
            if (pressed.getText().equals("Register")) {
                changeScreens("/fxml/register.fxml");
            } else {
                InetSocketAddress serverAddress = (InetSocketAddress) client.getServerManager().getService().getLocalAddress();
                client.getAuthorityManager().write(new LoginRequest(username.getText(), password.getText(), serverAddress.getPort()));
                error.setText("");
                login.setDisable(true);
                register.setDisable(true);
            }

        }
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
        if (event instanceof LoginEvent) {
            final LoginEvent loginEvent = (LoginEvent) event;
            Platform.runLater(
                    () -> {
                        if (loginEvent.isSuccessful()) {

                            try {
                                changeScreens("/fxml/chat.fxml");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                        	login.setDisable(false);
                            register.setDisable(false);
                            error.setText("Username or password is incorrect.");
                        }
                    }
            );
        }
    }
}
