package edu.iastate.cs.theseguys.database;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
	
	List<User> findByUserId(UUID id);
	
	boolean userExists(String username, String password);
	
	long getId(String username);
	
}
