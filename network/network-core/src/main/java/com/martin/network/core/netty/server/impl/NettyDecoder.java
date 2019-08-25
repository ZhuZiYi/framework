package com.martin.network.core.netty.server.impl;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
public class NettyDecoder extends MessageToMessageDecoder<ByteBuf> {
	private Class<?> packageClass;
	public NettyDecoder(Class<?> packageClass){
		this.packageClass = packageClass;
	}
	
	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf arg1, List<Object> arg2) throws Exception {
		// TODO Auto-generated method stub
		final byte[] array;
		final int length = arg1.readableBytes();
		array = new byte[ length ];
		arg1.getBytes(arg1.readerIndex(), array, 0,length);
		MessagePack msgPack =  new MessagePack();
		arg2.add( msgPack.read(array, packageClass));
	}

}
