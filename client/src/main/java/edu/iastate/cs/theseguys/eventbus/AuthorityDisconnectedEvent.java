package edu.iastate.cs.theseguys.eventbus;

public class AuthorityDisconnectedEvent extends AuthorityEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AuthorityDisconnectedEvent(Object source) {
        super(source);
    }
}
