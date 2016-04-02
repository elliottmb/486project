package edu.iastate.cs.theseguys.database;

import edu.iastate.cs.theseguys.hibernate.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends CrudRepository<Message, UUID> {
}
