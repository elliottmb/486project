package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.database.MessageRepository;
import edu.iastate.cs.theseguys.hibernate.Message;
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

    public Message getLatestMessage() {
        return repository.findFirstByOrderByTimestampDesc();
    }

    public boolean hasFather(Message m) {
        return repository.findOne(m.getFather().getId()) != null;
    }

    public boolean hasMother(Message m) {
        return repository.findOne(m.getMother().getId()) != null;
    }

    /**
     * Find two Messages with the least children possible.
     *
     * @return
     */
    public Pair<Message, Message> getYoungestCouple() {
        // TODO
        return null;
    }
}
