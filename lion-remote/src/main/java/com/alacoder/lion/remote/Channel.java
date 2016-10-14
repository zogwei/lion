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

import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: Channel
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午2:55:23
 *
 */

public interface Channel {
	
    boolean open();
    
    Response request(Request request) throws TransportException;
    
    boolean send(TransportData transportData) throws TransportException;
	
	void close();
	
	void close(int timeout);

	
	InetSocketAddress getLocalAddress();
	
	InetSocketAddress getRemoteAddress();
	
	LionURL getUrl();
    
    boolean isAvailable();
    
    boolean isClosed();
}
