package org.network.server;

import org.network.api.PackageDef;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.martin.network.core.MessageHandler;
import com.martin.network.core.ServerFactory;
import com.martin.network.core.netty.server.impl.NettyServer;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@Configuration
public class NetworkServerApplication 
{
	public static void main(String[] args) {
        SpringApplication.run(NetworkServerApplication.class, args);
	}

	@Bean
	ServerFactory serverFactory(){
		ServerFactory factory = new ServerFactory<TestMessageHandler,String>(NettyServer.class,new TestMessageHandler(),PackageDef.class);		
		factory.setPort(8070);
		return factory;
	}
}
