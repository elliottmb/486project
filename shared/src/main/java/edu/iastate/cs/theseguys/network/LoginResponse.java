package edu.iastate.cs.theseguys.network;

import java.security.PublicKey;
import java.util.UUID;

/**
 * AbstractMessage extension used to respond to a LoginRequest
 *
 */
public class LoginResponse extends AbstractMessage {
    private static final long serialVersionUID = -8424104713314817865L;
    private final boolean success;
    private final UUID assignedId;  //this is the id which the user's name/password are associated with in the Users table
    private final PublicKey pubKey;

    /**
     * Creates a LoginResponse with the given success, assignedId, and key
     * @param success
     * @param assignedId
     * @param key
     */
    public LoginResponse(boolean success, UUID assignedId, PublicKey key) {
        this.success = success;
        this.assignedId = assignedId;
        this.pubKey = key;
    }

    /**
     * Return the boolean success
     * @return success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Return the UUID assignedId
     * @return assignedId
     */
    public UUID getAssignedId() {
        return assignedId;
    }

    /**
     * returns the PublicKey pubKey
     * @return pubKey
     */
    public PublicKey getPublicKey() {
        return pubKey;
    }
	
	/**
	 * Returns success, assignedId, and pubKey as a LoginResponse string
	 */
    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + success +
                ", assignedId=" + assignedId +
                ", pubKey=" + pubKey +
                '}';
    }
}
