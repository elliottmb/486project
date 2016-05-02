package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.model.Peer;

import java.util.List;

/**
 * Sent by the authority to the client, contains a list of all clients currently connected to the authority
 *
 */
public class PeerListResponse extends AbstractMessage {
    private static final long serialVersionUID = -7705604556815520802L;
    private List<Peer> clients;

    /**
     * Creates a PeerListResponse object with the given list of peers/clients
     * @param clients
     */
    public PeerListResponse(List<Peer> clients) {
        this.clients = clients;
    }

    /**
     * Gets a list of current clients
     * @return list of clients/peers
     */
    public List<Peer> getClients() {
        return this.clients;
    }

    /**
     * Prints off list of clients as a peerlistresponse
     */
    @Override
    public String toString() {
        return "PeerListResponse{" +
                "clients=" + clients +
                '}';
    }
}
