package edu.iastate.cs.theseguys.network;

import java.util.List;

/**
 * Network request that asks for all
 * ancestors of the provided children
 */
public class AncestorsOfRequest extends AbstractMessage {
    private static final long serialVersionUID = 7470238964364602063L;
    private List<MessageDatagram> children;

    public AncestorsOfRequest(List<MessageDatagram> children) {
        this.children = children;
    }

    public List<MessageDatagram> getChildren() {
        return children;
    }
}
