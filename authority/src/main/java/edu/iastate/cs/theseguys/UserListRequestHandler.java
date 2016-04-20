package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.network.UserListRequest;
import edu.iastate.cs.theseguys.network.UserListResponse;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;


@Component
public class UserListRequestHandler implements MessageHandler<UserListRequest> {
    @Autowired
    private AuthorityClientManager manager;

    @Override
    public void handleMessage(IoSession session, UserListRequest request) throws Exception {
        Map<UUID, String> users = manager.getDatabaseManager().getAllUsers();

        session.write(new UserListResponse(users));
    }
}

