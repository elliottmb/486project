package edu.iastate.cs.theseguys.network;

public class PeerConnectionRequest extends AbstractMessage {
    private static final long serialVersionUID = -462688520305483484L;

    private int serverPort;

    public PeerConnectionRequest(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    @Override
    public String toString() {
        return "PeerConnectionRequest{" +
                "serverPort=" + serverPort +
                '}';
    }
}
