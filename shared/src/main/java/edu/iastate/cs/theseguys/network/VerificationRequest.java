package edu.iastate.cs.theseguys.network;

/**
 * Request the direct parents of the provided list of
 * child Messages.
 */
public class VerificationRequest extends AbstractMessage {
    private static final long serialVersionUID = -7719439373013433851L;
    private final MessageDatagram message;

    /**
     * Creates a VerificationRequest with the given message
     * @param message
     */
    public VerificationRequest(MessageDatagram message) {
        this.message = message;
    }

    /**
     * Gets the current message
     * @return message
     */
    public MessageDatagram getMessage() {
        return message;
    }

    /**
     * Returns a string of message as a verification request
     */
    @Override
    public String toString() {
        return "VerificationRequest{" +
                "message=" + message +
                '}';
    }
}
