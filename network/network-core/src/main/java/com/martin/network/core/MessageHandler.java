package com.martin.network.core;

import io.netty.channel.ChannelHandlerContext;

public abstract class MessageHandler {
	public abstract void onMessage(ChannelHandlerContext ctx, Object msg) throws Exception;
}
