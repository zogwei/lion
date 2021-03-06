/**
 * 版权声明：lion 版权所有 违者必究 2016
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

import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;

/**
 * @ClassName: AbstractClient
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月8日 下午3:31:50
 *
 */

public abstract class AbstractClient extends AbstractEndpoint implements Client {

	protected InetSocketAddress localAddress ;
	protected InetSocketAddress remoteAddress;
	
	protected LionURL url;
	protected Codec codec;
	
	protected int timeout;
    protected int  connectTimeout;
    
    protected MessageHandler messageHandler;

	
	public AbstractClient(LionURL url,MessageHandler messageHandler) {
		super(url);
		this.url = url;
		this.codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension(
                        url.getParameter(URLParamType.codec.getName(), URLParamType.codec.getValue()));
		Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(
				        url.getParameter(URLParamType.serialize.getName(), URLParamType.serialize.getValue()));
		
		codec.setSerialization(serialization);
		
		
        this.timeout = url.getIntParameter(URLParamType.timeout.getName(), URLParamType.timeout.getIntValue());
        this.connectTimeout = url.getIntParameter(URLParamType.connectTimeout.getName(), URLParamType.connectTimeout.getIntValue());
		
        this.messageHandler = messageHandler;
	}
	
	public abstract boolean open();
	
	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}
	
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}
	

}
