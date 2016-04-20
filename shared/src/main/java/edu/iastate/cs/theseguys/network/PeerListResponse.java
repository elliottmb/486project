package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.model.Peer;

import java.util.List;

public class PeerListResponse extends AbstractMessage {
    private static final long serialVersionUID = -7705604556815520802L;
    private List<Peer> clients;

    public PeerListResponse(List<Peer> clients) {
        this.clients = clients;
    }

    public List<Peer> getClients() {
        return this.clients;
    }

    @Override
    public String toString() {
        return "PeerListResponse{" +
                "clients=" + clients +
                '}';
    }
}
