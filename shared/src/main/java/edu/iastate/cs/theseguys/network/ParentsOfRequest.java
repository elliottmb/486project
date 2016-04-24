package edu.iastate.cs.theseguys.network;

import java.util.List;


/**
 * Request the direct parents of the provided list of
 * child Messages.
 */
public class ParentsOfRequest extends AbstractMessage {
    private static final long serialVersionUID = -1717707641974014227L;
    private final List<MessageDatagram> children;

    public ParentsOfRequest(List<MessageDatagram> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ParentsOfRequest{" +
                "children=" + children +
                '}';
    }

    public List<MessageDatagram> getChildren() {
        return children;
    }
}
