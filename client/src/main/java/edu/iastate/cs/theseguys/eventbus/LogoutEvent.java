package edu.iastate.cs.theseguys.eventbus;

/**
 * Event class for Logouts
 *
 */
public class LogoutEvent extends UserSessionEvent {
    private static final long serialVersionUID = -9142396819822460233L;
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

    /**
     * Returns confirmed as a LogoutEvent string
     */
    @Override
    public String toString() {
        return "LogoutEvent{" +
                "confirmed=" + confirmed +
                '}';
    }

    /**
     * Returns boolean confirmed
     * @return confirmed
     */
    public boolean isConfirmed() {
        return confirmed;
    }

}
