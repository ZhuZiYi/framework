package com.martin.network.core.netty.server.impl;

import javax.xml.transform.Templates;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyEncoder extends MessageToByteEncoder {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		MessagePack msgpack = new MessagePack();
        out.writeBytes(msgpack.write(msg));
	}

}
