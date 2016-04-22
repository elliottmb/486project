package edu.iastate.cs.theseguys.eventbus;

public class AuthorityConnectionFailedEvent extends AuthorityEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AuthorityConnectionFailedEvent(Object source) {
        super(source);
    }
}
