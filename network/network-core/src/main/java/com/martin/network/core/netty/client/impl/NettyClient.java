package com.martin.network.core.netty.client.impl;

import java.util.concurrent.TimeUnit;

import com.martin.network.core.MessageHandler;
import com.martin.network.core.netty.server.impl.NettyByteDecoder;
import com.martin.network.core.netty.server.impl.NettyDecoder;
import com.martin.network.core.netty.server.impl.NettyEncoder;
import com.martin.network.core.netty.server.impl.NettyHandler;
import com.martin.network.core.netty.server.impl.NettyServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class NettyClient<T extends MessageHandler> {
	private EventLoopGroup group;
	private Channel channel;
	private T onMessageHandler;
	private Class<?> packageClass;

	public void init(String address) throws Exception {

		Object[] array = address.split(":");
		String host = (String) array[0];
		int port = Integer.parseInt((String) array[1]);

		this.group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel channel) throws Exception {
				channel.pipeline().addLast(new IdleStateHandler(0, 0, 10, TimeUnit.MINUTES))
						.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2))
						
						.addLast("msgpack decoder", new NettyDecoder(packageClass))
						//.addLast("msgpack decoder1",new NettyByteDecoder())
						.addLast("frameEncoder", new LengthFieldPrepender(2))
						.addLast("msgpack encoder", new NettyEncoder())
						.addLast("nettyHandler", new NettyClientHandler<T>(onMessageHandler));

			}
		}).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
		this.channel = bootstrap.connect(host, port).sync().channel();

		log.debug(">>>>>>>>>>> xxl-rpc netty client proxy, connect to server success at host:{}, port:{}", host, port);
	}

	public void send(Object msg) throws Exception {
		this.channel.writeAndFlush(msg).sync();
	}
}
