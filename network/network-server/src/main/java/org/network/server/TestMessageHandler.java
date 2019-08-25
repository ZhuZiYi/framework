package org.network.server;

import org.network.api.PackageDef;

import com.martin.network.core.MessageHandler;

import io.netty.channel.ChannelHandlerContext;

public class TestMessageHandler extends MessageHandler {

	@Override
	public void onMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println("msg:" + msg);
		PackageDef pkg = (PackageDef)msg;
		pkg.setRspTimestamp(999);
		ctx.writeAndFlush(pkg);
	}
}
