package edu.iastate.cs.theseguys.eventbus;

public class LoginEvent extends UserSessionEvent {
    private final boolean successful;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public LoginEvent(Object source, boolean successful) {
        super(source);
        this.successful = successful;
    }

    @Override
    public String toString() {
        return "LoginEvent{" +
                "successful=" + successful +
                '}';
    }

    public boolean isSuccessful() {
        return successful;
    }
}
