package edu.iastate.cs.theseguys.network;

/**
 * Message sent from the authority to a client with the results of a RegisterRequest
 *
 */
public class RegisterResponse extends AbstractMessage {
    private static final long serialVersionUID = 6116255766837004104L;
    private final boolean success;
    private final String message;

    /**
     * Creates a RegisterResponse with the given success boolean and message
     * @param success
     * @param message
     */
    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Returns success
     * @return success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gets current message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the success and message variables as a RegisterResponse string
     */
    @Override
    public String toString() {
        return "RegisterResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
