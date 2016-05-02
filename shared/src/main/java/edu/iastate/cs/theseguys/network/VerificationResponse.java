package edu.iastate.cs.theseguys.network;

/**
 * Sent by the authority to a client, contains the result of the authority verification of the included message
 */
public class VerificationResponse extends AbstractMessage {
    private static final long serialVersionUID = -7719439373013433851L;
    private final MessageDatagram message;
    private final boolean valid;

    /**
     * Creates a VerificationResponse with the given message and 'valid' boolean indicating if the verification is valid
     * @param message
     * @param valid
     */
    public VerificationResponse(MessageDatagram message, boolean valid) {
        this.message = message;
        this.valid = valid;
    }

    /**
     * Returns the message and validity as a verification response
     */
    @Override
    public String toString() {
        return "VerificationResponse{" +
                "message=" + message +
                ", valid=" + valid +
                '}';
    }

    /**
     * Gets current message
     * @return message
     */
    public MessageDatagram getMessage() {
        return message;
    }

    /**
     * Returns boolean indicating validity
     * @return valid
     */
    public boolean isValid() {
        return valid;
    }
}
