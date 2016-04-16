package edu.iastate.cs.theseguys.client;

import java.net.InetSocketAddress;


import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import edu.iastate.cs.theseguys.network.LoggingMessageHandler;
import edu.iastate.cs.theseguys.network.LoginRequest;
import edu.iastate.cs.theseguys.network.LoginResponse;
import edu.iastate.cs.theseguys.network.RegisterRequest;
import edu.iastate.cs.theseguys.network.RegisterResponse;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class TestClient implements CommandLineRunner{
    private static final Logger log = LoggerFactory.getLogger(TestClient.class);

	
	public static void main(String[] args) {
        log.info("I touch myself");

        SpringApplication.run(TestClient.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        IoConnector connector = new NioSocketConnector();
        DemuxingIoHandler demuxIoHandler = new DemuxingIoHandler();



        demuxIoHandler.addSentMessageHandler(RegisterRequest.class, new LoggingMessageHandler());
        demuxIoHandler.addSentMessageHandler(LoginRequest.class, new LoggingMessageHandler());
        
        demuxIoHandler.addReceivedMessageHandler(RegisterResponse.class, new LoggingMessageHandler());
        demuxIoHandler.addReceivedMessageHandler(LoginResponse.class, new LoggingMessageHandler());

        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        connector.setHandler(demuxIoHandler);

        IoSession session;
        while (true) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress("localhost", 5050));
                future.awaitUninterruptibly();
                session = future.getSession();
                break;
            } catch (RuntimeIoException e) {
                System.err.println("Failed to connect.");
                e.printStackTrace();
                Thread.sleep(5000);
            }
        }

        session.write(new LoginRequest("barack", "obama", 1234));
       

        // wait until the summation is done
        session.getCloseFuture().awaitUninterruptibly();

        connector.dispose();
    }

}
