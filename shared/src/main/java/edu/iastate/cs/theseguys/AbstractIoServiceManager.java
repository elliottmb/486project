package edu.iastate.cs.theseguys;

import org.apache.mina.core.service.IoService;
import org.apache.mina.handler.demux.DemuxingIoHandler;

public class AbstractIoServiceManager<S extends IoService> {
    private S service;
    private DemuxingIoHandler demuxingIoHandler;

    public AbstractIoServiceManager(S service, DemuxingIoHandler demuxingIoHandler) {
        this.service = service;
        this.demuxingIoHandler = demuxingIoHandler;
        this.service.setHandler(demuxingIoHandler);
    }

    public S getService() {
        return service;
    }

    public DemuxingIoHandler getDemuxingIoHandler() {
        return demuxingIoHandler;
    }
    
    /**
     * Releases any resources allocated by this service.
     */
    public void dispose() {
        service.dispose();
    }

}
