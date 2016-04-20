package edu.iastate.cs.theseguys.network;

/**
 * Request the direct parents of the provided list of
 * child Messages.
 */
public class VerificationResponse extends AbstractMessage {
    private static final long serialVersionUID = -7719439373013433851L;
    private final MessageDatagram message;
    private final boolean valid;

    public VerificationResponse(MessageDatagram message, boolean valid) {
        this.message = message;
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "VerificationResponse{" +
                "message=" + message +
                ", valid=" + valid +
                '}';
    }

    public MessageDatagram getMessage() {
        return message;
    }

    public boolean isValid() {
        return valid;
    }
}
