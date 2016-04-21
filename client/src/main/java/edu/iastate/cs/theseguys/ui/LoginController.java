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

//TODO For chat use TextArea

@Component
public class LoginController implements ApplicationListener<UserSessionEvent> {

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

        if (event.getSource() instanceof Button) {
            Button pressed = (Button) event.getSource();
            Stage stage = (Stage) pressed.getScene().getWindow();
            Parent root = null;
            if (pressed.getText().equals("Register")) {
                root = springFXMLLoader.load("/fxml/register.fxml");
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                InetSocketAddress serverAddress = (InetSocketAddress) client.getServerManager().getService().getLocalAddress();
                client.getAuthorityManager().write(new LoginRequest(username.getText(), password.getText(), serverAddress.getPort()));
                // TODO: Disable buttons or indicate something here
            }

        }
    }

    @Override
    public void onApplicationEvent(UserSessionEvent event) {
        if (event instanceof LoginEvent) {
            LoginEvent loginEvent = (LoginEvent) event;
            if (loginEvent.isSuccessful()) {
                final Stage stage = (Stage) username.getScene().getWindow();
                Platform.runLater(
                        () -> {
                            try {
                                Parent root = springFXMLLoader.load("/fxml/chat.fxml");
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
            } else {
                // TODO: Undo the disabling and such done above, and maybe indicate failure
            }
        }
    }
}
