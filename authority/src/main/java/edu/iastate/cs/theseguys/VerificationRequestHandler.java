package edu.iastate.cs.theseguys;

import edu.iastate.cs.theseguys.network.VerificationRequest;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.MessageHandler;

public class VerificationRequestHandler implements MessageHandler<VerificationRequest> {

    private AuthorityClientManager manager;

    public VerificationRequestHandler(AuthorityClientManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void handleMessage(IoSession session, VerificationRequest request) throws Exception {
        //I guess we need to encode this message using the CentralAuthority's private key
        //and send the client a response containing that value


    }
}

