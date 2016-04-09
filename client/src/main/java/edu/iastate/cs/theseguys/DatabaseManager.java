package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.database.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseManager {


    @Autowired
    private MessageRepository repository;

    public MessageRepository getRepository() {
        return repository;
    }
}
