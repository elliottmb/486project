package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.hibernate.Message;

import java.util.List;


/**
 * Contains a list of Messages which are all parents of a given
 * set of Messages.  
 *
 */
public class ParentsOfResponse extends AbstractMessage {
	private static final long serialVersionUID = 987820442628229340L;
	private List<Message> parents;

    public ParentsOfResponse(List<Message> parents) {
        this.parents = parents;
    }

    public List<Message> getParents() {
        return parents;
    }
}
