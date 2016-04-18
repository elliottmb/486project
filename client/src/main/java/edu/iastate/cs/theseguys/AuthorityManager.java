package edu.iastate.cs.theseguys;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AuthorityManager extends AbstractIoConnectorManager {
    private static final Logger log = LoggerFactory.getLogger(AuthorityManager.class);

    public AuthorityManager() {
        super(new NioSocketConnector(), new DemuxingIoHandler());
    }

    @PostConstruct
    private void prepareHandlers() {
        getService().getFilterChain().addLast("logger", new LoggingFilter());
        getService().getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }
}
