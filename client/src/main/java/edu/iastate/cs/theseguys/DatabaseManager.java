package edu.iastate.cs.theseguys;

import com.google.common.collect.Iterables;
import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.database.MessageRepository;
import edu.iastate.cs.theseguys.database.QMessageRecord;
import edu.iastate.cs.theseguys.distributed.ClientManager;
import edu.iastate.cs.theseguys.distributed.ServerManager;
import edu.iastate.cs.theseguys.eventbus.NewMessageEvent;
import edu.iastate.cs.theseguys.network.MessageDatagram;
import edu.iastate.cs.theseguys.network.NewMessageAnnouncement;
import edu.iastate.cs.theseguys.network.ParentsOfRequest;
import javafx.util.Pair;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class DatabaseManager {
    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MessageRepository repository;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private ServerManager serverManager;
    private ConcurrentLinkedDeque<Pair<Long, MessageDatagram>> toProcess;
    private CopyOnWriteArrayList<Pair<AtomicLong, Pair<Long, MessageDatagram>>> waitingOnResponse;
    private LinkedHashSet<Pair<Long, MessageDatagram>> ready;
    private Thread processThread;
    private QueueProcessor queueProcessor;

    public DatabaseManager() {
        toProcess = new ConcurrentLinkedDeque<>();
        waitingOnResponse = new CopyOnWriteArrayList<>();
        ready = new LinkedHashSet<>();


        queueProcessor = new QueueProcessor();
        processThread = new Thread(queueProcessor);
        processThread.start();
    }

    public ConcurrentLinkedDeque<Pair<Long, MessageDatagram>> getToProcess() {
        return toProcess;
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
        return exists(m.getFather().getId());
    }

    public boolean hasMother(MessageRecord m) {
        return exists(m.getMother().getId());
    }

    public boolean exists(UUID id) {
        return getRepository().exists(id);
    }

    public synchronized void processQueue() {

        Iterator<Pair<Long, MessageDatagram>> readyIterator = ready.iterator();
        while (readyIterator.hasNext()) {
            Pair<Long, MessageDatagram> head = readyIterator.next();
            readyIterator.remove();
            MessageDatagram datagram = head.getValue();
            MessageRecord father = getRepository().findOne(datagram.getFatherId());
            MessageRecord mother = getRepository().findOne(datagram.getMotherId());

            log.info("Ready queue processing of " + datagram);
            log.info("Father result: " + father);
            log.info("Mother result: " + mother);

            // Check one last time that we don't already have this message
            if (getRepository().exists(datagram.getId())) {
                log.info("We already saved " + datagram);
                continue;
            }

            // Be sure!
            if (father == null || mother == null) {
                log.info("We have lost the mother or father of " + datagram);
                toProcess.add(head);
            } else {
                log.info("Saving record for " + datagram);

                MessageRecord newRecord = new MessageRecord(datagram.getId(), datagram.getUserId(), datagram.getMessageBody(), datagram.getTimestamp(), datagram.getSignature());
                newRecord.setFather(father);
                newRecord.setMother(mother);
                newRecord = getRepository().save(newRecord);
                applicationEventPublisher.publishEvent(new NewMessageEvent(this, newRecord));

                // If the message originated from this client, notify other clients!
                if (head.getKey() < 0L) {
                    serverManager.write(new NewMessageAnnouncement(datagram));
                }
            }
        }

        if (!waitingOnResponse.isEmpty()) {
            Pair<AtomicLong, Pair<Long, MessageDatagram>> head = waitingOnResponse.get(0);
            waitingOnResponse.remove(0);
            MessageDatagram headDatagram = head.getValue().getValue();

            if (exists(headDatagram.getFatherId()) && exists(headDatagram.getMotherId())) {
                log.info("Moving " + head.getValue().getValue().getId() + " to ready queue from waiting");
                ready.add(head.getValue());
            } else {
                long waited = head.getKey().getAndIncrement();

                //TODO: Handle the waiting period
                log.info("Readding " + head.getValue().getValue().getId() + " to waiting");
                waitingOnResponse.add(head);
            }
        }

        if (!toProcess.isEmpty()) {
            Pair<Long, MessageDatagram> head = toProcess.pop();

            MessageDatagram headDatagram = head.getValue();

            boolean readyHasFather = ready
                    .stream()
                    .anyMatch(
                            e -> e.getValue().getId() == headDatagram.getFatherId()
                    );

            boolean readyHasMother = ready
                    .stream()
                    .anyMatch(
                            e -> e.getValue().getId().equals(headDatagram.getMotherId())
                    );

            boolean waitingHasChild = waitingOnResponse
                    .stream()
                    .anyMatch(
                            e -> headDatagram.getId().equals(e.getValue().getValue().getFatherId()) || headDatagram.getId().equals(e.getValue().getValue().getMotherId())
                    );

            boolean waitingHasThis = waitingOnResponse
                    .stream()
                    .anyMatch(
                            e -> headDatagram.getId().equals(e.getValue().getValue().getId())
                    );

            log.info("toProcess: " + headDatagram.getId());
            log.info("readyHasFather: " + readyHasFather);
            log.info("readyHasMother: " + readyHasMother);
            log.info("waitingHasChild: " + waitingHasChild);
            log.info("waitingHasThis: " + waitingHasThis);

            // Make sure it doesn't already exist
            if (!exists(headDatagram.getId())) {
                log.info("We don't already have " + headDatagram.getId());
                // Check if we have both the father and mother already
                if (exists(headDatagram.getFatherId()) && exists(headDatagram.getMotherId())) {
                    log.info("We have the parents for " + headDatagram.getId());
                    // If we do, move this guy to the ready queue
                    log.info("Moving " + headDatagram.getId() + " to ready queue from to process");
                    ready.add(head);
                } else {
                    // Make sure father isn't processed or in the ready queue already
                    log.info("We don't have either the father or mother of " + headDatagram.getId());

                    if (!readyHasFather || !readyHasMother) {
                        log.info("We don't have both parents of " + headDatagram.getId());
                        IoSession session = clientManager.getSession(head.getKey());

                        if (session != null) {
                            if (waitingHasChild) {
                                session.write(new ParentsOfRequest(Collections.singletonList(headDatagram)));
                                // TODO: Fix AncestorsOfRequestHandler to not loop foreversss...
                                //session.write(new AncestorsOfRequest(Collections.singletonList(headDatagram)));
                            } else {
                                session.write(new ParentsOfRequest(Collections.singletonList(headDatagram)));
                            }
                        } else {
                            if (waitingHasChild) {
                                clientManager.write(new ParentsOfRequest(Collections.singletonList(headDatagram)));
                                // TODO: Fix AncestorsOfRequestHandler to not loop foreversss...
                                //clientManager.write(new AncestorsOfRequest(Collections.singletonList(headDatagram)));
                            } else {
                                log.warn("Connection with source for " + headDatagram.getId() + " has been lost, messaging all other clients");
                                clientManager.write(new ParentsOfRequest(Collections.singletonList(headDatagram)));
                            }
                        }
                        log.info("Requested parents for " + headDatagram.getId() + ", moving to the waiting queue");


                        if (!waitingHasThis) {
                            // Attach an AtomicLong to the pair to help track time in waiting queue
                            waitingOnResponse.add(new Pair<>(new AtomicLong(0), head));
                        }
                    }
                    // TODO: Other cases? I don't know. Probably not.

                }
            } else {
                log.info("We already have " + headDatagram.getId() + ", removing from queue");
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
        // We're guaranteed to have at least the root
        MessageRecord father = Iterables.getFirst(records, null);
        MessageRecord mother = Iterables.get(records, 1, father);

        return new Pair<>(father, mother);
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
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }
    }
}
