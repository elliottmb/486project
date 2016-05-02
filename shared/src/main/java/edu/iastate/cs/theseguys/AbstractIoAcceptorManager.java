package edu.iastate.cs.theseguys;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * Abstract class used for defining connection accepting/establishment
 *
 */
public abstract class AbstractIoAcceptorManager extends AbstractIoServiceManager<IoAcceptor, DemuxingIoHandler> {
    private static final Logger log = LoggerFactory.getLogger(AbstractIoAcceptorManager.class);

    /**
     * Creates an AbstractIoAcceptorManager with the given service and demuxingIoHandler
     * @param service
     * @param demuxingIoHandler
     */
    public AbstractIoAcceptorManager(IoAcceptor service, DemuxingIoHandler demuxingIoHandler) {
        super(service, demuxingIoHandler);
    }


    /**
     * Binds the service to the given SocketAddress localAddress
     * @param localAddress
     * @throws IOException
     */
    public void bind(SocketAddress localAddress) throws IOException {

        getService().bind(localAddress);
    }
}
