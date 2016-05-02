package edu.iastate.cs.theseguys.eventbus;

/**
 * Event class used when a register occurs
 *
 */
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

    /**
     * Gets the successful boolean
     * @return successful
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Gets the string message
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
