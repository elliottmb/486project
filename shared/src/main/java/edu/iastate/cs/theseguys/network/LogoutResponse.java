package edu.iastate.cs.theseguys.network;

/**
 * AbstractMessage extension used by the authority in response to a LogoutRequest from a connected client
 *
 */
public class LogoutResponse extends AbstractMessage {
    private static final long serialVersionUID = -510932825868392337L;
    //whether the authority confirmed the LogoutRequest which spawned this LogoutResponse
    private boolean confirmed;  

    /**
     * Creates a LogoutResponse with the given confirmed boolean
     * @param confirmed
     */
    public LogoutResponse(boolean confirmed) {
        this.confirmed = confirmed;
    }

    /**
     * Returns the confirmed boolean as a LogoutResponse string
     */
    @Override
    public String toString() {
        return "LogoutResponse{" +
                "confirmed=" + confirmed +
                '}';
    }

    /**
     * Returns the boolean confirmed
     * @return confirmed
     */
    public boolean isConfirmed() {
        return confirmed;
    }

}
