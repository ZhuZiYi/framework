package com.martin.network.core.netty.server.impl;

import java.util.concurrent.ThreadPoolExecutor;

import com.martin.network.core.MessageHandler;
import com.martin.network.core.Server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Data;

@Data
public class NettyHandler<T extends MessageHandler> extends ChannelInboundHandlerAdapter {
	private T messageHandler;
	private ThreadPoolExecutor serverHandlerPool;

	public NettyHandler(T messageHandler, final ThreadPoolExecutor serverHandlerPool) {
		this.messageHandler = messageHandler;
		this.serverHandlerPool = serverHandlerPool;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (messageHandler != null) {
			messageHandler.onMessage(ctx, msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

}
