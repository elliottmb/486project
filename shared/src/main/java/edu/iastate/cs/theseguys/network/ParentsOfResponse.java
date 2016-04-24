package edu.iastate.cs.theseguys.network;

import java.util.List;


/**
 * Contains a list of Messages which are all parents of a given
 * set of Messages.
 */
public class ParentsOfResponse extends AbstractMessage {
    private static final long serialVersionUID = 987820442628229340L;
    private final List<MessageDatagram> parents;

    public ParentsOfResponse(List<MessageDatagram> parents) {
        this.parents = parents;
    }

    @Override
    public String toString() {
        return "ParentsOfResponse{" +
                "parents=" + parents +
                '}';
    }

    public List<MessageDatagram> getParents() {
        return parents;
    }
}
