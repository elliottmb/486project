package edu.iastate.cs.theseguys.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends CrudRepository<MessageRecord, UUID> {

    List<MessageRecord> findByUserId(UUID uuid);

    List<MessageRecord> findByTimestamp(Timestamp timestamp);

    MessageRecord findFirstByOrderByTimestampAsc();

    MessageRecord findFirstByOrderByTimestampDesc();
}
