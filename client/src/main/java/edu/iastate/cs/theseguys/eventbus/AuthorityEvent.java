package edu.iastate.cs.theseguys.eventbus;

import org.springframework.context.ApplicationEvent;

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
