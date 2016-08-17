/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: Channel.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午2:55:23
 * @version V1.0
 */

package com.alacoder.lion.remote;

import java.net.InetSocketAddress;

import com.alacoder.lion.common.url.URL;

/**
 * @ClassName: Channel
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午2:55:23
 *
 */

public interface Channel {

	InetSocketAddress getLocalAddress();
	
	InetSocketAddress getRemoteAddress();
	
	void close();
	
	void close(int timeout);
	
	void send(TransportData data);
	
	URL getUrl();
}
