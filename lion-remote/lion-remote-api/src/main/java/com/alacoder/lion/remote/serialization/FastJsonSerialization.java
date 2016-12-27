/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: FastJsonSerialization.java
 * @Package com.alacoder.lion.remote.serialization
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:29:35
 * @version V1.0
 */

package com.alacoder.lion.remote.serialization;

import java.io.IOException;

import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.remote.Serialization;
import com.alibaba.fastjson.JSON;

/**
 * @ClassName: FastJsonSerialization
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:29:35
 *
 */
@SpiMeta( name  = "fastJson")
public class FastJsonSerialization implements Serialization{

	
	@Override
	public byte[] serialize(Object msg) throws IOException {
		String out = JSON.toJSONString(msg);
		return out.getBytes();
	}

	
	@Override
	public <T> T deserialize(byte[] bytes, Class<T> cls) throws IOException {
		String jsonString = new String(bytes);
		return JSON.parseObject(jsonString, cls);
	}

}
