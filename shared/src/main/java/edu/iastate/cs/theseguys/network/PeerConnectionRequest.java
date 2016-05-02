package edu.iastate.cs.theseguys.network;

/**
 * AbstractMessage extension used to request connection to a peer
 * 
 */
public class PeerConnectionRequest extends AbstractMessage {
    private static final long serialVersionUID = -462688520305483484L;

    private int serverPort;

    /**
     * Creates a PeerConnectionRequest with the given serverPort
     * @param serverPort
     */
    public PeerConnectionRequest(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Gets the serverPort
     * @return serverPort
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Returns the serverPort as a PeerConnectionRequest
     */
    @Override
    public String toString() {
        return "PeerConnectionRequest{" +
                "serverPort=" + serverPort +
                '}';
    }
}
