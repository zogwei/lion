/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: Serialization.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:25:07
 * @version V1.0
 */

package com.alacoder.lion.remote;

import java.io.IOException;

import com.alacoder.lion.common.extension.Scope;
import com.alacoder.lion.common.extension.Spi;

/**
 * @ClassName: Serialization
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:25:07
 *
 */
@Spi(scope=Scope.PROTOTYPE)
public interface Serialization {

	byte[] serialize(Object msg) throws IOException;
	
	<T> T deserialize(byte[] bytes, Class<T> cls) throws IOException;
}
