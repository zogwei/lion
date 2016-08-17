/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: AbstractServer.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午4:55:53
 * @version V1.0
 */

package com.alacoder.lion.remote;

import java.net.InetSocketAddress;

import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.url.URL;
import com.alacoder.lion.common.url.URLParamType;

/**
 * @ClassName: AbstractServer
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午4:55:53
 *
 */

public abstract class AbstractServer implements Server {

	protected InetSocketAddress localAddress ;
	protected InetSocketAddress remoteAddress;
	
	protected URL url;
	protected Codec codec;
	
    protected volatile ChannelState state = ChannelState.UNINIT;
	
	public AbstractServer(URL url) {
		this.url = url;
		this.codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension(
                        url.getParameter(URLParamType.codec.getName(), URLParamType.codec.getValue()));
		Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(
				        url.getParameter(URLParamType.serialize.getName(), URLParamType.serialize.getValue()));
		
		codec.setSerialization(serialization);
		
		try{
			doOpen();
		} catch ( Throwable t ){
			t.printStackTrace();
		}
		
	}
	
	public abstract void doOpen();
	
	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}
	
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

}
