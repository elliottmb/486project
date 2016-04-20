package edu.iastate.cs.theseguys.database;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DatabaseManager {

	
	private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

	//@Autowired
	private BCryptPasswordEncoder encoder;

	
	
	public DatabaseManager()
	{
		encoder = new BCryptPasswordEncoder();
	}

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
    	user.setPassword(encoder.encode(user.getPassword()));
		repository.save(user);
	}
    
    public boolean userExists(String username)
    {
    	boolean result = repository.existsByUsername(username); 
    	log.info("USER "+username+" EXISTS? "+ result);
    	
    	return result;
    }
    
    /**
     * Returns the user with the specified username and password if it exists, or null otherwise
     * @param username
     * @param password
     * @return
     */
    public User authenticateUser(String username, String password)
    {
    	User u = repository.findByUsername(username);
    	if (u==null)
    	{
    		return null;
    	}
    	else if(!encoder.matches(password, u.getPassword()))
    	{
    		return null;
    	}
    	return u;
    }
    
    public Map<UUID, String> getAllUsers()
    {
    	Iterable<User> users = repository.findAll();
    	Map<UUID, String> result = new LinkedHashMap<UUID, String>();
    	for(User u : users)
    	{
    		result.put(u.getId(), u.getUsername());
    	}
    	
    	return result;
    }
    
}