package edu.iastate.cs.theseguys.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class DatabaseManager {
    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserRepository repository;

    public DatabaseManager() {
        encoder = new BCryptPasswordEncoder();
    }

    public UserRepository getRepository() {
        return repository;
    }

    public UUID getUserId(String username) {
        //get the id assigned to this user in the database
        return repository.getId(username);
    }

    public void insertUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
    }

    public boolean userExists(String username) {
        boolean result = repository.existsByUsername(username);
        log.info("USER " + username + " EXISTS? " + result);

        return result;
    }

    /**
     * Returns the user with the specified username and password if it exists, or null otherwise
     *
     * @param username
     * @param password
     * @return
     */
    public User authenticateUser(String username, String password) {
        User user = repository.findByUsername(username);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public Map<UUID, String> getAllUsers() {
        return StreamSupport
                .stream(
                        repository.findAll()
                                .spliterator(),
                        false
                )
                .collect(
                        Collectors.toMap(User::getId, User::getUsername)
                );
    }

}