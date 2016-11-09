/**
 * 版权声明：lion 版权所有 违者必究 2016
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

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.StandardThreadExecutor;

/**
 * @ClassName: AbstractServer
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午4:55:53
 *
 */

public abstract class AbstractServer extends AbstractEndpoint implements Server {

//	protected InetSocketAddress localAddress ;
	protected InetSocketAddress remoteAddress;
	
	protected LionURL url;
	protected Codec codec;
	protected MessageHandler messagehandler ;
	
	protected StandardThreadExecutor standardThreadExecutor = null;
	
    protected volatile ChannelState state = ChannelState.UNINIT;
	
	public AbstractServer(LionURL url,MessageHandler messagehandler) {
		super(url);
		this.url = url;
		this.codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension(
                        url.getParameter(URLParamType.codec.getName(), URLParamType.codec.getValue()));
		Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(
				        url.getParameter(URLParamType.serialize.getName(), URLParamType.serialize.getValue()));
		
		codec.setSerialization(serialization);
		this.messagehandler = messagehandler;
	}
	
	public abstract boolean open();
	
	public InetSocketAddress getLocalAddress() {
		throw new LionFrameworkException("Server  getLocalAddress() method unsupport: url: " + url);
	}
	
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

}
