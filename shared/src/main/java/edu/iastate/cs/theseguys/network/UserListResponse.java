package edu.iastate.cs.theseguys.network;

import java.util.Map;
import java.util.UUID;

/**
 * AbstractMessage extension used to respond to a UserListRequest
 *
 */
public class UserListResponse extends AbstractMessage {
    private static final long serialVersionUID = -8033950939300333602L;
    private Map<UUID, String> users;

    /**
     * Creates a UserListResponse with the given users (as a Map<UUID, String>)
     * @param users
     */
    public UserListResponse(Map<UUID, String> users) {
        this.users = users;
    }

    /**
     * Returns users map
     * @return users
     */
    public Map<UUID, String> getUsers() {
        return users;
    }

    /**
     * Returns the user map as a UserListResponse
     */
    @Override
    public String toString() {
        return "UserListResponse{" +
                "users=" + users +
                '}';
    }
}
