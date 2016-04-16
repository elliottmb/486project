package edu.iastate.cs.theseguys.network;

import java.util.List;

/**
 * Network message that contains a List of messages
 * representing the ancestors of a given set of messages
 */
public class AncestorsOfResponse extends AbstractMessage {
    private static final long serialVersionUID = -2008354896340491100L;
    private List<MessageDatagram> ancestors;

    public AncestorsOfResponse(List<MessageDatagram> ancestors) {
        this.ancestors = ancestors;
    }

    public List<MessageDatagram> getAncestors() {
        return ancestors;
    }
}
