package edu.iastate.cs.theseguys.eventbus;

import org.springframework.context.ApplicationEvent;


public abstract class UserSessionEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public UserSessionEvent(Object source) {
        super(source);
    }

    @Override
    public String toString() {
        return "UserSessionEvent{}";
    }
}
