package edu.iastate.cs.theseguys.database;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DatabaseManager {

	private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);


    @Autowired
    private UserRepository repository;

    public UserRepository getRepository() {
        return repository;
    }
    
    public UUID getUserId(String username)
	{
		//get the id assigned to this user in the database
		return repository.getId(username);
	}
    
    public void insertUser(User user)
	{
		repository.save(user);
	}
    
    public boolean userExists(String username)
    {
    	boolean result =repository.existsByUsername(username); 
    	log.info("USER "+username+" EXISTS? "+ result);
    	
    	return result;
    }
}