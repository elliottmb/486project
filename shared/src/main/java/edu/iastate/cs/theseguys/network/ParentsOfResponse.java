package edu.iastate.cs.theseguys.network;

import java.util.List;


/**
 * Contains a list of Messages which are all parents of a given
 * set of Messages.
 */
public class ParentsOfResponse extends AbstractMessage {
    private static final long serialVersionUID = 987820442628229340L;
    private final List<MessageDatagram> parents;

    /**
     * Creates a ParentsOfResponse with the given MessageDatagram parents
     * @param parents
     */
    public ParentsOfResponse(List<MessageDatagram> parents) {
        this.parents = parents;
    }

    /**
     * Returns the parents as a ParentsOfResponse
     */
    @Override
    public String toString() {
        return "ParentsOfResponse{" +
                "parents=" + parents +
                '}';
    }

    /**
     * Gets the MessageDatagram parents
     * @return
     */
    public List<MessageDatagram> getParents() {
        return parents;
    }
}
