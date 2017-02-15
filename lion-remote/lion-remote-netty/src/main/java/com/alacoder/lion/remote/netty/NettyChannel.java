/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyChannel.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 上午11:09:34
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;

import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.ExceptionUtil;
import com.alacoder.lion.remote.ChannelState;
import com.alacoder.lion.remote.Endpoint;
import com.alacoder.lion.remote.Future;
import com.alacoder.lion.remote.FutureListener;
import com.alacoder.lion.remote.ResponseFuture;
import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.TransportException;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: NettyChannel
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 上午11:09:34
 *
 */

public class NettyChannel extends com.alacoder.lion.remote.AbstractChannel{
	
	private final static LogService logger = LogFactory.getLogService(NettyChannel.class);
	
	private volatile ChannelState state = ChannelState.UNINIT;
	
	private Endpoint endpoint;
	private io.netty.channel.Channel channel = null;
	
	private InetSocketAddress remoteAddress = null;
	private InetSocketAddress localAddress = null;

	public NettyChannel(Endpoint endpoint) {
		super(endpoint.getUrl());
		this.endpoint = endpoint;
		state = ChannelState.INIT;
	}
	
	public NettyChannel(Endpoint endpoint,io.netty.channel.Channel channel) {
		super(endpoint.getUrl());
		this.endpoint = endpoint;
		this.channel = channel;
		state = ChannelState.INIT;
	}
	
	
	
	@Override
	public synchronized boolean open() {
		if(isAvailable()) {
			logger.warn("the channel already open, local: " + localAddress + " remote: " + remoteAddress + " url: " + endpoint.getUrl().getUri());
			return true;
		}
		
		if(state.isInitState()){
			state = ChannelState.ALIVE;
			logger.debug("NettyChannel open: " + endpoint.getUrl().getUri() + " local=" + channel.localAddress());
		}
		else {
			throw new LionFrameworkException("NettyChannel is not  init,can not  open,now state is : " + state);
		}
		
		return true;
	}

	@Override
	public Response<?> request(Request<?> request) throws TransportException {
		
		int timeout = endpoint.getUrl().getIdentityParameter(request.getIdentity(),URLParamType.requestTimeout.getName(), URLParamType.requestTimeout.getIntValue());
		
		if (timeout <= 0) {
               throw new LionFrameworkException("NettyClient init Error: timeout(" + timeout + ") <= 0 is forbid.", LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
        }
		
		ResponseFuture response = new NettyResponseFuture(request, timeout, this.endpoint);
		this.endpoint.registerCallback(request.getId(), response);
		
		ChannelFuture writeFuture = this.channel.writeAndFlush(request);
		
		boolean result = writeFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
		
		// TODO writeFuture 存在序列号异常时 丢失异常信息
		if(result && writeFuture.isSuccess()) {
			response.addListener(new FutureListener(){
				@Override
				public void operationComplete(Future future) throws Exception {
					if (future.isSuccess() || (future.isDone() && ExceptionUtil.isBizException(future.getException()))) {
						// 成功的调用 
						endpoint.resetErrorCount();
					} else {
						// 失败的调用 
						endpoint.incrErrorCount();
					}
				}
			});
			
			return response;
		}
		else {
			response = this.endpoint.removeCallback(request.getId());

			if (response != null) {
				response.cancel();
			}

			// 失败的调用 
			endpoint.incrErrorCount();
			
			throw new LionServiceException("NettyChannel send request to server Error: url=" + endpoint.getUrl().getUri() + " local=" + localAddress + " "+ request);
		}
	}

	@Override
	public boolean send(TransportData transportData) throws TransportException {
		int timeout = endpoint.getUrl().getIntParameter(URLParamType.timeout.getName(), URLParamType.timeout.getIntValue());
		
		//TODO requestid is null
		ChannelFuture writeFuture = this.channel.writeAndFlush(transportData);
		boolean result = writeFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
		if(result && writeFuture.isSuccess()) {
			logger.debug("NettyChannel send success: " + endpoint.getUrl().getUri() + " local=" + localAddress);
		}
		else {
			logger.debug("NettyChannel send fail: " + endpoint.getUrl().getUri() + " local=" + localAddress);
		}
		return result;
	}
	
	@Override
	public synchronized void close() {
		close(0);
	}

	@Override
	public synchronized void close(int timeout) {
		try {
			state = ChannelState.CLOSE;

			if (channel != null) {
				channel.close();
			}
		} catch (Exception e) {
			logger.error("NettyChannel close Error: " + endpoint.getUrl().getUri() + " local=" + localAddress, e);
		}
	}
	
	@Override
	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	@Override
	public LionURL getUrl() {
		return endpoint.getUrl();
	}

	@Override
	public boolean isAvailable() {
		return state.isAliveState();
	}

	@Override
	public boolean isClosed() {
		return state.isCloseState();
	}

}
