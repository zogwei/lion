/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: Endpoint.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月6日 上午10:55:12
 * @version V1.0
 */

package com.alacoder.lion.remote.transport;

import com.alacoder.lion.remote.Channel;

/**
 * @ClassName: Endpoint
 * @Description: TODO
 * @author jimmy.zhong
 * @date 2016年8月6日 上午10:55:12
 *
 */

public interface Endpoint extends Channel{

	void request();
	
}
