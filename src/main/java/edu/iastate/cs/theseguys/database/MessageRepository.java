package edu.iastate.cs.theseguys.database;

import edu.iastate.cs.theseguys.hibernate.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends CrudRepository<Message, UUID> {

    List<Message> findByUserId(UUID uuid);

    List<Message> findByTimestamp(Timestamp timestamp);
}
