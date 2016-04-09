package edu.iastate.cs.theseguys.network;

/**
 * Request for the most recently added message from 
 * a client.  Client A sends one of these whenever it
 * connects to Client B.  B will respond with a 
 * LatestMessageResponse containing B's most recent message
 *
 */
public class LatestMessageRequest extends AbstractMessage {
    private static final long serialVersionUID = 4698211896586557828L;

    public LatestMessageRequest() {

    }
}
