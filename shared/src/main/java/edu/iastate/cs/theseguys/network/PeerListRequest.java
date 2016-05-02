package edu.iastate.cs.theseguys.network;

/**
 * Used to request a list of current peers
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
