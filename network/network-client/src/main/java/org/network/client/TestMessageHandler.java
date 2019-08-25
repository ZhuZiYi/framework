package org.network.client;

import org.network.api.PackageDef;

import com.martin.network.core.MessageHandler;

import io.netty.channel.ChannelHandlerContext;

public class TestMessageHandler extends MessageHandler {

	@Override
	public void onMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		PackageDef pkg = (PackageDef)msg;
		System.out.print("#############CLIENT msg:" + pkg);
	}

}
