package edu.iastate.cs.theseguys;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;

public abstract class AbstractIoAcceptorManager extends AbstractIoServiceManager<IoAcceptor, DemuxingIoHandler> {
    private static final Logger log = LoggerFactory.getLogger(AbstractIoAcceptorManager.class);

    public AbstractIoAcceptorManager(IoAcceptor service, DemuxingIoHandler demuxingIoHandler) {
        super(service, demuxingIoHandler);
    }


    public void bind(SocketAddress localAddress) throws IOException {

        getService().bind(localAddress);
    }
}
