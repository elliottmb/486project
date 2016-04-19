package edu.iastate.cs.theseguys.network;


public class LogoutResponse extends AbstractMessage {
    private static final long serialVersionUID = -510932825868392337L;
    private boolean confirmed;

    public LogoutResponse(boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public String toString() {
        return "LogoutResponse{" +
                "confirmed=" + confirmed +
                '}';
    }

    public boolean isConfirmed() {
        return confirmed;
    }

}
