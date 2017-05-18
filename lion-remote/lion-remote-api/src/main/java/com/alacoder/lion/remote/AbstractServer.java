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
import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
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
	
	private final static Log logger = LogFactory.getLog(AbstractServer.class);

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
	
	protected abstract void doOpen();
	
	protected abstract void doClose(int timeout);
	
	public InetSocketAddress getLocalAddress() {
		throw new LionFrameworkException("Server  getLocalAddress() method unsupport: url: " + url);
	}
	
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}
	
	public synchronized boolean open() {
		if(state != ChannelState.UNINIT) {
			logger.error("NettyServer is not in init state, open error, url = {} ", url.getUri());
			return false;
		}
		 doOpen();
		 
		state = ChannelState.ALIVE;
		logger.info("NettyServer open success , url = {}", url.getUri());

		return true;
	}
	
	public void close() {
		close(0);
		
	}
	
	public void close(int timeout) {
		//判断系统server是否关闭
		if (state.isCloseState()) {
			logger.info("NettyServer close fail: already close, url={}", url.getUri());
			return;
		}

		//是否初始化
		if (state.isUnInitState()) {
			logger.info("NettyServer close Fail: don't need to close because node is unInit state: url={}", url.getUri());
			return;
		}
		
		//关闭threadpool相关资源
		try{
			standardThreadExecutor.shutdownNow();
		} catch(Throwable e) {
			logger.info("NettyServer close Fail: close error: url=" + url.getUri(),e);
		}
		
		doClose(timeout);
		
		// 设置close状态
		state = ChannelState.CLOSE;

	}

}
