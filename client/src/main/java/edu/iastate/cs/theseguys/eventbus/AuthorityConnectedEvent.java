package edu.iastate.cs.theseguys.eventbus;

public class AuthorityConnectedEvent extends AuthorityEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AuthorityConnectedEvent(Object source) {
        super(source);
    }
}
