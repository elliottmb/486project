package edu.iastate.cs.theseguys.distributed;


import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import org.apache.mina.core.session.IoSession;

public class ClientDemuxingIoHandler extends ManagedDemuxingIoHandler {


    @Override
    public void sessionCreated(IoSession session) throws Exception {
        session.write(new LatestMessageRequest());
    }

}
