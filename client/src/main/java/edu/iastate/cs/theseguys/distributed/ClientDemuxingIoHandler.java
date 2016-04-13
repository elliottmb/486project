package edu.iastate.cs.theseguys.distributed;


import edu.iastate.cs.theseguys.network.LatestMessageRequest;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.DemuxingIoHandler;

public class ClientDemuxingIoHandler extends DemuxingIoHandler {


    @Override
    public void sessionCreated(IoSession session) throws Exception {
        session.write(new LatestMessageRequest());
    }

}
