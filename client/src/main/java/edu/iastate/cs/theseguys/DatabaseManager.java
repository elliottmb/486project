package edu.iastate.cs.theseguys;

import com.google.common.collect.Iterables;
import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.database.MessageRepository;
import edu.iastate.cs.theseguys.database.QMessageRecord;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseManager {
    @Autowired
    private MessageRepository repository;

    public MessageRepository getRepository() {
        return repository;
    }

    // TODO: Abstract business logic around repository here, such that nothing has direct access to the repository

    public MessageRecord getLatestMessage() {
        return repository.findFirstByOrderByTimestampDesc();
    }

    public boolean hasFather(MessageRecord m) {
        return repository.findOne(m.getFather().getId()) != null;
    }

    public boolean hasMother(MessageRecord m) {
        return repository.findOne(m.getMother().getId()) != null;
    }

    /**
     * Find two Messages with the least children possible.
     *
     * @return
     */
    public Pair<MessageRecord, MessageRecord> getMostFruitlessTwoRecords() {
        Iterable<MessageRecord> records = getRecordsByHowFruitless();
        return new Pair<>(Iterables.getFirst(records, null), Iterables.get(records, 1, null));
    }

    /**
     * Returns all MessageRecords by count of children, ascending.
     *
     * @return all entities
     */
    public Iterable<MessageRecord> getRecordsByHowFruitless() {
        QMessageRecord message = QMessageRecord.messageRecord;
        return repository.findAll(message.leftChildren.size().add(message.rightChildren.size()).asc());
    }

    /**
     * Returns a MessageRecords with the least amount of children.
     *
     * @return all entities
     */
    public MessageRecord getMostFruitlessRecord() {
        return Iterables.getFirst(getRecordsByHowFruitless(), null);
    }
}
