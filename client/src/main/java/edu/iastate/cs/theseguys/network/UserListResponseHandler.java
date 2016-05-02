package edu.iastate.cs.theseguys.network;

import edu.iastate.cs.theseguys.DatabaseManager;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/**
 * Handler class for UserListResponse
 * Gets the user list for the response
 *
 */
public class UserListResponseHandler implements MessageHandler<UserListResponse> {
    private static final Logger log = LoggerFactory.getLogger(UserListResponseHandler.class);

    @Autowired
    private DatabaseManager databaseManager;

    @Override
    /**
     * Method that gets the current userlist
     * @param session
     * @param response
     */
    public void handleMessage(IoSession session, UserListResponse response) throws Exception {
        log.info("Received " + response);

        databaseManager.getKnownUsernames().putAll(response.getUsers());
    }

}
