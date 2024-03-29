package edu.iastate.cs.theseguys;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Abstract class used for defining basic connection behavior
 *
 */
public abstract class AbstractIoConnectorManager extends AbstractIoServiceManager<IoConnector, DemuxingIoHandler> {
    private static final Logger log = LoggerFactory.getLogger(AbstractIoConnectorManager.class);

    /**
     * Creates an AbstractIoConnectorManager with the given service and demuxingIoHandler
     * @param service
     * @param demuxingIoHandler
     */
    public AbstractIoConnectorManager(IoConnector service, DemuxingIoHandler demuxingIoHandler) {
        super(service, demuxingIoHandler);
    }

    /**
     * This function attempts to connect to the given address. This method is non-blocking and returns the base
     * ConnectFuture.
     *
     * @param host Server to connect to
     * @return ConnectFuture
     */
    public ConnectFuture connect(InetSocketAddress host) {
        log.info("Attempting to connect to " + host.getHostName() + ":" + host.getPort());

        return getService().connect(host);
    }
}
