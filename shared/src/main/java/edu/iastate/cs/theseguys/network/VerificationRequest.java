package edu.iastate.cs.theseguys.network;

/**
 * Request the direct parents of the provided list of
 * child Messages.
 */
public class VerificationRequest extends AbstractMessage {
    private static final long serialVersionUID = -7719439373013433851L;
    private final MessageDatagram message;

    public VerificationRequest(MessageDatagram message) {
        this.message = message;
    }

    public MessageDatagram getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "VerificationRequest{" +
                "message=" + message +
                '}';
    }
}
