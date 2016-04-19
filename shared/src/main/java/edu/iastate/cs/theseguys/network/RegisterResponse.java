package edu.iastate.cs.theseguys.network;


public class RegisterResponse extends AbstractMessage {
    private static final long serialVersionUID = 6116255766837004104L;
    private final boolean success;
    private final String message;

    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
