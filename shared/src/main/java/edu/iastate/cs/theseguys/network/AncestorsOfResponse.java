package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.hibernate.Message;

import java.util.List;

/**
 * Network message that contains a List of messages
 * representing the ancestors of a given set of messages
 *
 */
public class AncestorsOfResponse extends AbstractMessage {
    private static final long serialVersionUID = -2008354896340491100L;
    private List<Message> ancestors;

    public AncestorsOfResponse(List<Message> ancestors) {
        this.ancestors = ancestors;
    }

    public List<Message> getAncestors() {
        return ancestors;
    }
}
