package edu.iastate.cs.theseguys.network;

import java.util.List;


/**
 * Request the direct parents of the provided list of
 * child Messages.
 */
public class ParentsOfRequest extends AbstractMessage {
    private static final long serialVersionUID = -1717707641974014227L;
    private final List<MessageDatagram> children;

    /**
     * Creates a ParentsOfRequest with the given MessageDatagram children
     * @param children
     */
    public ParentsOfRequest(List<MessageDatagram> children) {
        this.children = children;
    }

    /**
     * Returns the children as a ParentsOfRequest
     */
    @Override
    public String toString() {
        return "ParentsOfRequest{" +
                "children=" + children +
                '}';
    }

    /**
     * Gets the MessageDatagram children
     * @return
     */
    public List<MessageDatagram> getChildren() {
        return children;
    }
}
