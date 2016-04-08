package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.hibernate.Message;

import java.io.Serializable;
import java.util.List;

public class ParentsOfRequest implements Serializable {
    private List<Message> children;

    public ParentsOfRequest(List<Message> children) {
        this.children = children;
    }

    public List<Message> getChildren() {
        return children;
    }
}
