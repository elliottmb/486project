package edu.iastate.cs.theseguys.database;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Interface to access the Client's database.
 */
@Repository
public interface MessageRepository extends CrudRepository<MessageRecord, UUID>, QueryDslPredicateExecutor<MessageRecord> {

    List<MessageRecord> findByUserId(UUID uuid);

    List<MessageRecord> findByTimestamp(Timestamp timestamp);

    MessageRecord findFirstByOrderByTimestampAsc();

    MessageRecord findFirstByOrderByTimestampDesc();

    List<MessageRecord> findFirst10ByOrderByTimestampDesc();
}
