package edu.iastate.cs.theseguys.network;

/**
 * Sent by a client to the authority to request a list of all other currently active peers
 *
 */
public class PeerListRequest extends AbstractMessage {
    private static final long serialVersionUID = -4070310026858103559L;

    /**
     * Creates empty PeerListRequest object
     */
    public PeerListRequest() {

    }

    /**
     * Returns "PeerListRequest{}"
     */
    @Override
    public String toString() {
        return "PeerListRequest{}";
    }


}
