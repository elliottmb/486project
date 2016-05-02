package edu.iastate.cs.theseguys.eventbus;

import org.springframework.context.ApplicationEvent;

/**
 * Abstract class defining some basic characteristics of user session events
 *
 */
public abstract class UserSessionEvent extends ApplicationEvent {
    private static final long serialVersionUID = -1917186373047532040L;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public UserSessionEvent(Object source) {
        super(source);
    }

    /**
     * Returns usersessionevent string
     */
    @Override
    public String toString() {
        return "UserSessionEvent{}";
    }
}
