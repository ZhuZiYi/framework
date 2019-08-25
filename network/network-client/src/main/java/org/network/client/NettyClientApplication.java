package org.network.client;

import java.util.HashMap;

import org.msgpack.template.Templates;
import org.network.api.PackageDef;
import com.martin.network.core.netty.client.impl.NettyClient;

import cn.hutool.json.JSONObject;


/**
 * Hello world!
 *
 */
public class NettyClientApplication 
{
    public static void main( String[] args )
    {
        NettyClient client = new NettyClient<TestMessageHandler>();
        try {
        	client.setOnMessageHandler(new TestMessageHandler());
        	client.setPackageClass(PackageDef.class);
			client.init("localhost:8070");
			
			
			PackageDef pkg = new PackageDef();
			pkg.setUuid(1).setReqTimestamp(100).setVersion("v1.0").setBody(new JSONObject().put("id", 1).put("name", "Martin").toString());
			
					
			client.send(pkg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
