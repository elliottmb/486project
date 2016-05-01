package edu.iastate.cs.theseguys;

import com.google.common.collect.Iterables;
import edu.iastate.cs.theseguys.database.MessageRecord;
import edu.iastate.cs.theseguys.database.MessageRepository;
import edu.iastate.cs.theseguys.database.QMessageRecord;
import edu.iastate.cs.theseguys.distributed.ClientManager;
import edu.iastate.cs.theseguys.distributed.ServerManager;
import edu.iastate.cs.theseguys.eventbus.NewMessageEvent;
import edu.iastate.cs.theseguys.network.AncestorsOfRequest;
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

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

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
    private LinkedHashMap<Pair<Long, MessageDatagram>, Long> waitingOnResponse;
    private LinkedHashSet<Pair<Long, MessageDatagram>> ready;
    private LinkedHashSet<Pair<Long, MessageDatagram>> needAncestors;
    private LinkedHashSet<Pair<Long, MessageDatagram>> needParents;
    private Thread processThread;
    private QueueProcessor queueProcessor;

    private Map<UUID, String> knownUsernames;

    public DatabaseManager() {
        toProcess = new ConcurrentLinkedDeque<>();
        waitingOnResponse = new LinkedHashMap<>();
        ready = new LinkedHashSet<>();
        needAncestors = new LinkedHashSet<>();
        needParents = new LinkedHashSet<>();
        knownUsernames = new LinkedHashMap<>();

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

        if (ready.size() > 0) {
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
        }

        if (waitingOnResponse.size() > 0) {
            Iterator<Map.Entry<Pair<Long, MessageDatagram>, Long>> waitingIterator = waitingOnResponse.entrySet().iterator();

            while (waitingIterator.hasNext()) {
                Map.Entry<Pair<Long, MessageDatagram>, Long> head = waitingIterator.next();
                MessageDatagram headDatagram = head.getKey().getValue();

                boolean readyHasFather = ready
                        .stream()
                        .anyMatch(
                                e -> e.getValue().getId().equals(headDatagram.getFatherId())
                        );

                boolean readyHasMother = ready
                        .stream()
                        .anyMatch(
                                e -> e.getValue().getId().equals(headDatagram.getMotherId())
                        );

                if ((exists(headDatagram.getFatherId()) || readyHasFather) && (exists(headDatagram.getMotherId()) || readyHasMother)) {
                    log.info("waitingQueue: Moving " + headDatagram.getId() + " to ready queue");
                    ready.add(head.getKey());

                    waitingIterator.remove();
                } else {
                    long waited = head.getValue();

                    if (System.currentTimeMillis() - waited > 30000) {
                        log.info("waitingQueue: Waited too long, moving " + headDatagram.getId() + " back to toProcess");
                        toProcess.addLast(head.getKey());
                        waitingIterator.remove();
                    } else {

                        //TODO: Handle the waiting period
                        log.info("waitingQueue: Leaving " + headDatagram.getId() + " in waiting queue");
                    }
                }
            }
        }
        Iterator<Pair<Long, MessageDatagram>> toProcessIterator = toProcess.iterator();
        while (toProcessIterator.hasNext()) {
            Pair<Long, MessageDatagram> head = toProcessIterator.next();
            toProcessIterator.remove();

            MessageDatagram headDatagram = head.getValue();

            log.info("toProcess: " + headDatagram.getId());

            // Make sure it doesn't already exist
            if (!exists(headDatagram.getId())) {
                log.info("We don't already have " + headDatagram.getId());
                boolean readyHasFather = ready
                        .stream()
                        .anyMatch(
                                e -> e.getValue().getId().equals(headDatagram.getFatherId())
                        );

                boolean readyHasMother = ready
                        .stream()
                        .anyMatch(
                                e -> e.getValue().getId().equals(headDatagram.getMotherId())
                        );

                // Check to see if we have the father and mother ready to save or already saved
                if ((exists(headDatagram.getFatherId()) || readyHasFather) && (exists(headDatagram.getMotherId()) || readyHasMother)) {
                    // If we do, move this guy to the ready queue
                    log.info("toProcess: father or mother " + headDatagram.getId() + " already saved or in ready queue");
                    ready.add(head);
                } else {
                    // Make sure father isn't processed or in the ready queue already
                    log.info("toProcess: father or mother " + headDatagram.getId() + " NOT saved or in ready queue");

                    boolean waitingHasThis = waitingOnResponse
                            .keySet()
                            .stream()
                            .anyMatch(
                                    e -> headDatagram.getId().equals(e.getValue().getId())
                            );

                    if (waitingHasThis) {
                        log.info("toProcess: Waiting already has " + headDatagram.getId());

                    } else {
                        log.info("toProcess: Waiting DOES NOT have " + headDatagram.getId());

                        boolean waitingHasFather = waitingOnResponse
                                .keySet()
                                .stream()
                                .anyMatch(
                                        e -> e.getValue().getId().equals(headDatagram.getFatherId())
                                );

                        boolean waitingHasMother = waitingOnResponse
                                .keySet()
                                .stream()
                                .anyMatch(
                                        e -> e.getValue().getId().equals(headDatagram.getMotherId())
                                );

                        if (!((readyHasFather || waitingHasFather) && (readyHasMother || waitingHasMother))) {
                            log.info("toProcess: missing a parent of  " + headDatagram.getId());
                            IoSession session = clientManager.getSession(head.getKey());

                            boolean waitingHasChild = waitingOnResponse
                                    .keySet()
                                    .stream()
                                    .anyMatch(
                                            e -> headDatagram.getId().equals(e.getValue().getFatherId()) || headDatagram.getId().equals(e.getValue().getMotherId())
                                    );

                            if (waitingHasChild) {
                                needAncestors.add(head);
                            } else {
                                needParents.add(head);
                            }
                        }
                    }
                    // TODO: Other cases? I don't know. Probably not.

                }
            } else {
                log.info("We already have " + headDatagram.getId() + ", removing from queue");
            }
        }

        if (!needAncestors.isEmpty()) {
            Map<Long, List<Pair<Long, MessageDatagram>>> collect = needAncestors
                    .stream()
                    .collect(
                            Collectors.groupingBy(Pair::getKey)
                    );

            for (Map.Entry<Long, List<Pair<Long, MessageDatagram>>> entry : collect.entrySet()) {
                IoSession session = clientManager.getSession(entry.getKey());

                List<MessageDatagram> toSend = entry.getValue()
                        .stream()
                        .map(Pair::getValue)
                        .collect(Collectors.toList());

                if (session != null && session.isConnected()) {
                    session.write(new AncestorsOfRequest(toSend));
                } else {
                    log.warn("toProcess: Connection with source for " + entry.getValue() + " has been lost, messaging all other clients");
                    clientManager.write(new AncestorsOfRequest(toSend));
                }

                log.info("toProcess: Requested ancestors for " + entry.getValue() + ", moving to the waiting queue");

                long timeRequested = System.currentTimeMillis();
                // Attach an AtomicLong to the pair to help track time in waiting queue
                for (Pair<Long, MessageDatagram> pair : entry.getValue()) {
                    waitingOnResponse.put(pair, timeRequested);
                }
            }

            needAncestors.clear();
        }

        if (!needParents.isEmpty()) {

            Map<Long, List<Pair<Long, MessageDatagram>>> collect = needParents
                    .stream()
                    .collect(
                            Collectors.groupingBy(Pair::getKey)
                    );

            for (Map.Entry<Long, List<Pair<Long, MessageDatagram>>> entry : collect.entrySet()) {
                IoSession session = clientManager.getSession(entry.getKey());

                List<MessageDatagram> toSend = entry.getValue()
                        .stream()
                        .map(Pair::getValue)
                        .collect(Collectors.toList());

                if (session != null && session.isConnected()) {
                    session.write(new ParentsOfRequest(toSend));
                } else {
                    log.warn("toProcess: Connection with source for " + entry.getValue() + " has been lost, messaging all other clients");
                    clientManager.write(new ParentsOfRequest(toSend));
                }
                log.info("toProcess: Requested parents for " + entry.getValue() + ", moving to the waiting queue");

                long timeRequested = System.currentTimeMillis();
                // Attach an AtomicLong to the pair to help track time in waiting queue
                for (Pair<Long, MessageDatagram> pair : entry.getValue()) {
                    waitingOnResponse.put(pair, timeRequested);
                }
            }

            needParents.clear();
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

    public synchronized Map<UUID, String> getKnownUsernames() {
        return knownUsernames;
    }

    public synchronized void setKnownUsernames(Map<UUID, String> knownUsernames) {
        this.knownUsernames = knownUsernames;
    }

    private class QueueProcessor implements Runnable {

        private volatile boolean running = true;

        private long lastDelayed;

        public QueueProcessor() {
            lastDelayed = System.currentTimeMillis();
        }

        public void terminate() {
            running = false;
        }

        public void run() {
            while (running) {
                if (toProcess.size() > 0 || ready.size() > 0) {
                    lastDelayed = System.currentTimeMillis();
                }
                processQueue();
                if (System.currentTimeMillis() - lastDelayed > 1000) {
                    try {
                        log.info("Delaying processing queue");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        running = false;
                    }
                }
            }
        }
    }
}
