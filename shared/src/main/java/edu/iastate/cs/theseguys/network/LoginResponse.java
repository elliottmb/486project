package edu.iastate.cs.theseguys.network;

import java.security.PublicKey;
import java.util.UUID;

public class LoginResponse extends AbstractMessage {
    private static final long serialVersionUID = -8424104713314817865L;
    private final boolean success;
    private final UUID assignedId;  //this is the id which the user's name/password are associated with in the Users table
    private final PublicKey pubKey;

    public LoginResponse(boolean success, UUID assignedId, PublicKey key) {
        this.success = success;
        this.assignedId = assignedId;
        this.pubKey = key;
    }

    public boolean isSuccess() {
        return success;
    }

    public UUID getAssignedId() {
        return assignedId;
    }

    public PublicKey getPublicKey() {
        return pubKey;
    }


    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + success +
                ", assignedId=" + assignedId +
                ", pubKey=" + pubKey +
                '}';
    }
}
