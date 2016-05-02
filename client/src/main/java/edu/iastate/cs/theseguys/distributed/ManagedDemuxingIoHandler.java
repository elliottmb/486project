package edu.iastate.cs.theseguys.distributed;

import org.apache.mina.handler.demux.DemuxingIoHandler;

/**
 * Abstract handler class for ManagedDemuxingIo
 *
 */
public abstract class ManagedDemuxingIoHandler extends DemuxingIoHandler {

    private ClientManager clientManager;
    private ServerManager serverManager;

    /**
     * Gets the ClientManager clientManager
     * @return clientManager
     */
    public ClientManager getClientManager() {
        return clientManager;
    }

    /**
     * Sets the ClientManager clientManager
     * @param clientManager
     */
    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    /**
     * Gets the ServerManager serverManager
     * @return serverManager
     */
    public ServerManager getServerManager() {
        return serverManager;
    }

    /**
     * Sets the ServerManager serverManager
     * @param serverManager
     */
    public void setServerManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }
}
