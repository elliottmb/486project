package edu.iastate.cs.theseguys.distributed;

import org.apache.mina.handler.demux.DemuxingIoHandler;


public abstract class ManagedDemuxingIoHandler extends DemuxingIoHandler {

    private ClientManager clientManager;
    private ServerManager serverManager;

    public ClientManager getClientManager() {
        return clientManager;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public void setServerManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }
}
