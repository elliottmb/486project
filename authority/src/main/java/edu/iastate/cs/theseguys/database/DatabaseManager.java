package edu.iastate.cs.theseguys.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseManager {


    @Autowired
    private UserRepository repository;

    public UserRepository getRepository() {
        return repository;
    }
}