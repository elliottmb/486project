package edu.iastate.cs.theseguys;

import com.google.common.collect.Iterables;
import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.database.MessageRepository;
import edu.iastate.cs.theseguys.database.QMessageRecord;
import edu.iastate.cs.theseguys.distributed.ClientManager;
import edu.iastate.cs.theseguys.network.MessageDatagram;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class DatabaseManager {
    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);
    @Autowired
    private MessageRepository repository;
    @Autowired
    private ClientManager clientManager;
    private ConcurrentLinkedDeque<Pair<Long, MessageDatagram>> waiting;
    private ConcurrentLinkedDeque<Pair<Long, MessageDatagram>> ready;
    private Thread processThread;
    private QueueProcessor queueProcessor;

    public DatabaseManager() {
        waiting = new ConcurrentLinkedDeque<>();
        ready = new ConcurrentLinkedDeque<>();


        queueProcessor = new QueueProcessor();
        processThread = new Thread(queueProcessor);
        processThread.start();
    }

    public ConcurrentLinkedDeque<Pair<Long, MessageDatagram>> getWaiting() {
        return waiting;
    }

    public ConcurrentLinkedDeque<Pair<Long, MessageDatagram>> getReady() {
        return ready;
    }

    public void dispose() {
        try {
            queueProcessor.terminate();
            processThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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

    public synchronized void processQueue() {
        //log.info("Processing...");
        if (!waiting.isEmpty()) {
            Pair<Long, MessageDatagram> head = waiting.pop();

            MessageDatagram headDatagram = head.getValue();

            // Make sure it doesn't already exist
            if (!getRepository().exists(headDatagram.getId())) {
                log.info("We don't already have " + headDatagram.getId());
                // Check if we have both the father and mother already
                if (getRepository().exists(headDatagram.getFatherId()) && getRepository().exists(headDatagram.getMotherId())) {
                    log.info("We have the parents for " + headDatagram.getId());
                    // If we do, move this guy to the ready queue
                    ready.add(head);
                } else {
                    // Make sure father isn't processed or in the ready queue already
                    if (!getRepository().exists(headDatagram.getFatherId())
                            && ready
                            .stream()
                            .filter(
                                    e -> e.getValue().getId() == headDatagram.getFatherId()
                            )
                            .count() == 0
                            ) {
                        //TODO: Send request for father
                        log.info("We don't have the father of " + headDatagram.getId());
                    }
                    if (!getRepository().exists(headDatagram.getFatherId())
                            && ready
                            .stream()
                            .filter(
                                    e -> e.getValue().getId() == headDatagram.getMotherId()
                            )
                            .count() == 0
                            ) {
                        //TODO: Send request for mother
                        log.info("We don't have the mother of " + headDatagram.getId());
                    }
                    // TODO: Other cases? I don't know. Probably not. (╯°□°)╯︵ ┻━┻
                    log.info("Adding back to the end of the queue, " + headDatagram.getId());
                    waiting.add(head);
                }
            }
        }
        if (!ready.isEmpty()) {
            Pair<Long, MessageDatagram> head = ready.pop();
            MessageDatagram datagram = head.getValue();
            MessageRecord father = getRepository().findOne(datagram.getFatherId());
            MessageRecord mother = getRepository().findOne(datagram.getMotherId());

            // Be sure!
            if (father == null || mother == null) {
                log.info("We don't have the mother or father of " + datagram.getId());
                waiting.add(head);
            } else {
                MessageRecord newRecord = new MessageRecord(datagram.getId(), datagram.getUserId(), datagram.getMessageBody(), datagram.getTimestamp(), datagram.getSignature());
                newRecord.setFather(father);
                newRecord.setMother(mother);
                getRepository().save(newRecord);
                log.info("Saving record for " + datagram.getId());
            }
        }
    }

    /**
     * Find two Messages with the least children possible.
     *
     * @return
     */
    public Pair<MessageRecord, MessageRecord> getIdealParentRecords() {
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

    protected class QueueProcessor implements Runnable {

        private volatile boolean running = true;

        public QueueProcessor() {
        }

        public void terminate() {
            running = false;
        }

        public void run() {
            while (running) {
                processQueue();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }
    }
}
