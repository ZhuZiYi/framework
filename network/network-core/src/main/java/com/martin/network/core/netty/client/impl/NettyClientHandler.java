package com.martin.network.core.netty.client.impl;

import java.util.concurrent.ThreadPoolExecutor;

import com.martin.network.core.MessageHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler<T extends MessageHandler> extends SimpleChannelInboundHandler {

	private T messageHandler;
	public NettyClientHandler(T messageHandler){
		this.messageHandler  = messageHandler;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		if (messageHandler != null ){
			messageHandler.onMessage(ctx, msg);
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
}
