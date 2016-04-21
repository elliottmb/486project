package edu.iastate.cs.theseguys.eventbus;

public class RegisterEvent extends UserSessionEvent {
    private final boolean successful;
    private final String message;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RegisterEvent(Object source, boolean successful, String message) {
        super(source);
        this.successful = successful;
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }
}
