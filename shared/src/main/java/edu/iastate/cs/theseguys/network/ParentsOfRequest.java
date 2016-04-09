package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.hibernate.Message;

import java.util.List;

public class ParentsOfRequest extends AbstractMessage {
    private static final long serialVersionUID = -1717707641974014227L;
    private List<Message> children;

    public ParentsOfRequest(List<Message> children) {
        this.children = children;
    }

    public List<Message> getChildren() {
        return children;
    }
}
