package com.martin.network.core.netty.server.impl;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.martin.network.core.MessageHandler;
import com.martin.network.core.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class NettyServer<T extends MessageHandler> extends Server<T> {
	private Thread thread;

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				final ThreadPoolExecutor serverHandlerPool = makeServerThreadPool(NettyServer.class.getSimpleName());
				EventLoopGroup bossGroup = new NioEventLoopGroup();
				EventLoopGroup workerGroup = new NioEventLoopGroup();

				try {
					ServerBootstrap bootstrap = new ServerBootstrap();
					bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
							.childHandler(new ChannelInitializer<SocketChannel>() {
								@Override
								public void initChannel(SocketChannel channel) throws Exception {
									channel.pipeline().addLast(new IdleStateHandler(0, 0, 10, TimeUnit.MINUTES))
									.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(1024, 0, 2,0,2))
									//LengthFieldBasedFrameDecoder基类是ByteToMessage 已经bytebuf读取过了，所以后面的应该继MessgeToMMessage 否则会出错 		
									.addLast("msgpack decoder",new NettyDecoder(getPackageClass()))
									//.addLast("msgpack decoder1",new NettyByteDecoder())
									.addLast("frameEncoder",new LengthFieldPrepender(2))
									.addLast("msgpack encoder",new NettyEncoder())
									.addLast("nettyHandler",new NettyHandler<T>(getOnMessageFunctionHandler(),makeServerThreadPool(NettyServer.class.getName())));
							
								}
							}).childOption(ChannelOption.TCP_NODELAY, true)
							.childOption(ChannelOption.SO_KEEPALIVE, true);

					ChannelFuture future = bootstrap.bind(getPort()).sync();

					log.info("##### remoting server start success, nettype = {}, port = {}",
							NettyServer.class.getName(), getPort());
					onStart();
					future.channel().closeFuture().sync();

				} catch (Exception e) {
					if (e instanceof InterruptedException) {
						log.info("##### remoting server stop.");
					} else {
						log.error("##### remoting server error.", e);
					}
				} finally {
					try {
						serverHandlerPool.shutdown();
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
					try {
						workerGroup.shutdownGracefully();
						bossGroup.shutdownGracefully();
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		});
		thread.setDaemon(true);
		thread.start();

	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
		}

		// on stop
		onStop();
		log.info("##### remoting server destroy success.");
	}

}
