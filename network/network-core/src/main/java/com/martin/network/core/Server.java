package com.martin.network.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author zhubing
 * servre abstart class
 *
 */
@Data
@Slf4j
public abstract class Server<T extends MessageHandler> {
	private FunctionHandler startFunctionHandler;
	private FunctionHandler stopFunctionHandler;
	private T onMessageFunctionHandler;
	private int port;
	public Class<?> packageClass;

	public abstract void start() throws Exception;

	public abstract void stop() throws Exception;

	public void onStart() {
		if (startFunctionHandler != null) {
			try {
				startFunctionHandler.run();
			} catch (Exception e) {
				log.error("#### server startFunctionHandler error.", e);
			}
		}
	}

	public void onStop() {
		if (stopFunctionHandler != null) {
			try {
				stopFunctionHandler.run();
			} catch (Exception e) {
				log.error("#### server stopFunctionHandler error.", e);
			}
		}
	}
	
	public static ThreadPoolExecutor makeServerThreadPool(final String serverType){
        ThreadPoolExecutor serverHandlerPool = new ThreadPoolExecutor(
                60,
                300,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "##### "+serverType+"-serverHandlerPool-" + r.hashCode());
                    }
                },
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        throw new RuntimeException("##### "+serverType+" Thread pool is EXHAUSTED!");
                    }
                });

        return serverHandlerPool;
    }

}
