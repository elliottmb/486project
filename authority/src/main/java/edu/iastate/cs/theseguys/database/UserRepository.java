package edu.iastate.cs.theseguys.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
	
	/**
	 * Get the unique user ID of the user with the provided username
	 */
    @Query("select u.id from User u where u.username=?1")
    UUID getId(String username);

    /**
     * Return the User with the provided username
     */
    User findByUsername(String username);

    /**
     * Return true if the database contains a user with the provided username, false otherwise
     */
    @Query("select case when count(u) > 0 then true else false end from User u where u.username = ?1")
    boolean existsByUsername(String username);

}
