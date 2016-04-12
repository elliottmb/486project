package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.database.MessageRepository;
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
    public Pair<MessageRecord, MessageRecord> getYoungestCouple() {
        // TODO
        return null;
    }
}
