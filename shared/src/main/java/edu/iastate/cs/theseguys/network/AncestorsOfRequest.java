package edu.iastate.cs.theseguys.network;

import java.util.List;

/**
 * Network request that asks for all
 * ancestors of the provided children
 */
public class AncestorsOfRequest extends AbstractMessage {
    private static final long serialVersionUID = 7470238964364602063L;
    private List<MessageDatagram> children;

    /**
     * Creates a new AncestorsOfRequest with the given MessageDatagram list children
     * @param children
     */
    public AncestorsOfRequest(List<MessageDatagram> children) {
        this.children = children;
    }

    /**
     * Gets the MessageDatagram list children
     * @return children
     */
    public List<MessageDatagram> getChildren() {
        return children;
    }

    /**
     * Returns the MessageDatagram list children as an AncestorsOfRequest string
     */
    @Override
    public String toString() {
        return "AncestorsOfRequest{" +
                "children=" + children +
                '}';
    }
}
