package edu.iastate.cs.theseguys.eventbus;

public class LogoutEvent extends UserSessionEvent {
    private final boolean confirmed;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public LogoutEvent(Object source, boolean confirmed) {
        super(source);
        this.confirmed = confirmed;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
