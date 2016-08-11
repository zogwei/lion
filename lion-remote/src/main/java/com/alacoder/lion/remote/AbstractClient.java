/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: AbstractClient.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月8日 下午3:31:50
 * @version V1.0
 */

package com.alacoder.lion.remote;

import java.net.InetSocketAddress;

/**
 * @ClassName: AbstractClient
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月8日 下午3:31:50
 *
 */

public abstract class AbstractClient implements Client {

	protected InetSocketAddress localAddress ;
	protected InetSocketAddress remoteAddress;
	
	
	
	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}
	
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}
	

}
