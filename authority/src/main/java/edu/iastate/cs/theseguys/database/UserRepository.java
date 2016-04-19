package edu.iastate.cs.theseguys.database;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
	
//	@Query("SELECT * FROM users WHERE users.id = :id")
//	List<User> findByUserId(UUID id);
	
//	@Query("SELECT * FROM users WHERE users.username = :username AND users.password = :password")
//	List<User> findUser(String username, String password);
//	
//	@Query("SELECT id FROM users WHERE users.username = :username")
//	long getId(String username);
	
	@Query("select u.id from User u where u.username=?1")
	UUID getId(String username);
	
	User findByUsername(String username);
	
	@Query("select case when count(u) > 0 then true else false end from User u where u.username = ?1")
	boolean existsByUsername(String username);
	
}
