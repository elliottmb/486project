package edu.iastate.cs.theseguys.network;

/**
 * AbstractMessage extension used to request a list of current users
 *
 */
public class UserListRequest extends AbstractMessage {
    private static final long serialVersionUID = -7485099387823396797L;

    /**
     * Creates a basic UserListRequest
     */
    public UserListRequest() {

    }

    /**
     * Returns a UserListRequest as a string
     */
    @Override
    public String toString() {
        return "UserListRequest{}";
    }
}
