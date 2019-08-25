package org.network.api;

import java.io.Serializable;
import java.util.Map;

import org.msgpack.annotation.Message;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Message
public class PackageDef implements Serializable {
	private int uuid;
	private int reqTimestamp;
	private int rspTimestamp;
	private String version;
	private int type;//协议类型
	private String  body;//存josn字符串
}
