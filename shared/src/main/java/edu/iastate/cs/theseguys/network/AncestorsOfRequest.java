package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.hibernate.Message;

import java.util.List;

public class AncestorsOfRequest extends AbstractMessage {
    private static final long serialVersionUID = 7470238964364602063L;
    private List<Message> children;

    public AncestorsOfRequest(List<Message> children) {
        this.children = children;
    }

    public List<Message> getChildren() {
        return children;
    }
}
