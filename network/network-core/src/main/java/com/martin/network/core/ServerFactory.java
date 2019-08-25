package com.martin.network.core;

import org.springframework.beans.factory.InitializingBean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ServerFactory<T extends MessageHandler,E> implements InitializingBean{
	private Server server;
	private Class<? extends Server> serverClass;
	private T onMessageFunctionHandler;
	private Class<?> packageClass;
	
	public ServerFactory(Class<? extends Server> serverClass,T onMessageFunctionHandler,Class<?> packageClass){
		this.serverClass = serverClass;
		this.onMessageFunctionHandler = onMessageFunctionHandler;
		this.packageClass = packageClass;
	}
	
	private int port;
	

	public void start() throws Exception {
		server = serverClass.<T,E>newInstance();
		server.setPort(port);
		server.setOnMessageFunctionHandler(onMessageFunctionHandler);
		server.setPackageClass(packageClass);
		server.setStartFunctionHandler(new FunctionHandler(){

			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				log.debug("##### ServerFactory StartFunction called!");
			}			
		});
		
		
		server.setStopFunctionHandler(new FunctionHandler(){

			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				log.debug("##### ServerFactory StopFunction called!");
			}
			
		});
		
		//启动服务器
		server.start();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		start();
	}

}
