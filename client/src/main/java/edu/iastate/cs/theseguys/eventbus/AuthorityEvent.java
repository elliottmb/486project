package edu.iastate.cs.theseguys.eventbus;

import org.springframework.context.ApplicationEvent;

/**
 * Abstract event class for dealing with Authority
 *
 */
public abstract class AuthorityEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AuthorityEvent(Object source) {
        super(source);
    }
}
