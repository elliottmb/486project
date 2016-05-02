package edu.iastate.cs.theseguys;

import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
/**
 * 
 *Class used to load FXML content
 *
 */
public class SpringFXMLLoader {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Loads content based on given url
     * @param url
     * @return
     * @throws IOException
     */
    public <T> T load(String url) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(clazz -> applicationContext.getBean(clazz));
        fxmlLoader.setLocation(getClass().getResource(url));
        return fxmlLoader.load();
    }
}