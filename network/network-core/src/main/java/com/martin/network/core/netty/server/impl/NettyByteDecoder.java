package com.martin.network.core.netty.server.impl;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

public class NettyByteDecoder extends MessageToMessageDecoder<String> {

	@Override
	protected void decode(ChannelHandlerContext ctx, String in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("HHAHAHA#########");
		out.add(in);
	}

}
