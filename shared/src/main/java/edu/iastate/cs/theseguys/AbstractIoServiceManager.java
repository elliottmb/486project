package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.network.AbstractMessage;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class AbstractIoServiceManager<S extends IoService, I extends IoHandler> {
    private static final Logger log = LoggerFactory.getLogger(AbstractIoServiceManager.class);
    private S service;
    private I ioHandler;

    public AbstractIoServiceManager(S service, I demuxingIoHandler) {
        this.service = service;
        this.ioHandler = demuxingIoHandler;
        this.service.setHandler(demuxingIoHandler);
    }

    /**
     * Writes the specified <code>message</code> to every currently connected remote peer.
     *
     * @param message The message to write
     * @return The list of associated WriteFutures
     */
    public List<WriteFuture> write(AbstractMessage message) {
        log.info("Attempting to write " + message.toString() + " to each connected peer");
        return getService()
                .getManagedSessions()
                .entrySet()
                .parallelStream()
                .filter(
                        entry -> entry.getValue().isConnected()
                )
                .map(
                        entry -> entry.getValue().write(message)
                )
                .collect(Collectors.toList());
    }

    public S getService() {
        return service;
    }

    public I getIoHandler() {
        return ioHandler;
    }

    /**
     * Releases any resources allocated by this service.
     */
    public void dispose() {
        // Make sure we close all sessions before disposing
        service.getManagedSessions().forEach(
                (e, b) -> {
                    b.closeOnFlush().awaitUninterruptibly();
                }
        );
        service.dispose();
    }

}
