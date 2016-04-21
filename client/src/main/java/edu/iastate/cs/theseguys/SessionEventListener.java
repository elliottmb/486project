package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.eventbus.UserSessionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SessionEventListener implements ApplicationListener<UserSessionEvent> {

    private static final Logger log = LoggerFactory.getLogger(SessionEventListener.class);

    @Override
    public void onApplicationEvent(UserSessionEvent event) {
        log.info("Got event: " + event);
    }
}
