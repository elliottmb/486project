package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.network.UserListRequest;
import edu.iastate.cs.theseguys.network.UserListResponse;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;

import java.util.Map;
import java.util.UUID;


public class UserListRequestHandler implements MessageHandler<UserListRequest> {

    private AuthorityClientManager manager;

    public UserListRequestHandler(AuthorityClientManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void handleMessage(IoSession session, UserListRequest request) throws Exception {
        Map<UUID, String> users = manager.getDatabaseManager().getAllUsers();

        session.write(new UserListResponse(users));
    }
}

