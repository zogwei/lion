/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: Codec.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午5:01:56
 * @version V1.0
 */

package com.alacoder.lion.remote;

import java.io.IOException;

import com.alacoder.lion.common.extension.Spi;
import com.alacoder.lion.common.extension.Scope;

/**
 * @ClassName: Codec
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午5:01:56
 *
 */
@Spi(scope=Scope.PROTOTYPE)
public interface Codec {

	byte[] encode(Object message) throws IOException;
	
	Object decode(byte[] buffer) throws IOException;
	
	Serialization getSerialization();
	
	void setSerialization(Serialization serialization);
}
