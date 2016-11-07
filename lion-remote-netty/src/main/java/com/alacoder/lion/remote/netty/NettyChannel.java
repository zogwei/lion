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
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.ExceptionUtil;
import com.alacoder.lion.common.utils.LoggerUtil;
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
	private volatile ChannelState state = ChannelState.UNINIT;
	
	private Endpoint endpoint;
	private io.netty.channel.Channel channel = null;
	
	private InetSocketAddress remoteAddress = null;
	private InetSocketAddress localAddress = null;

	public NettyChannel(Endpoint endpoint) {
		this.endpoint = endpoint;
	}
	
	public NettyChannel(Endpoint endpoint,io.netty.channel.Channel channel) {
		this.endpoint = endpoint;
		this.channel = channel;
	}
	
	@Override
	public synchronized boolean open() {
		ChannelFuture channelFuture = null;
		if(isAvailable()) {
			LoggerUtil.warn("the channel already open, local: " + localAddress + " remote: " + remoteAddress + " url: " + endpoint.getUrl().getUri());
			return true;
		}
		
		if(endpoint instanceof NettyClient ){
			NettyClient client = (NettyClient)endpoint;
			try{
				channelFuture = client.getClient().connect(new InetSocketAddress(endpoint.getUrl().getHost(), endpoint.getUrl().getPort()));
				
				long start = System.currentTimeMillis();
				
				int timeout = endpoint.getUrl().getIntParameter(URLParamType.connectTimeout.getName(), URLParamType.connectTimeout.getIntValue());
				if (timeout <= 0) {
		            throw new LionFrameworkException("NettyClient init Error: timeout(" + timeout + ") <= 0 is forbid.",
		                    LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
				}
				
				boolean result = channelFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
				boolean success = channelFuture.isSuccess();
				
				if (result && success) {
					channel = channelFuture.channel();
					if (channel.localAddress() != null && channel.localAddress() instanceof InetSocketAddress) {
						localAddress = (InetSocketAddress) channel.localAddress();
						this.remoteAddress =  new InetSocketAddress(endpoint.getUrl().getHost(), endpoint.getUrl().getPort());
					}

					state = ChannelState.ALIVE;
					return true;
				}
				else {
					boolean connected = false;
		            if(channelFuture.channel() != null){
		            	channel = channelFuture.channel();
		                connected = channelFuture.channel().isActive();
		            }
		            throw new LionServiceException("NettyChannel connect to server timeout url: "
	                        + endpoint.getUrl().getUri() + ", cost: " + (System.currentTimeMillis() - start) + ", result: " + result + ", success: " + success + ", connected: " + connected);
				}
			} catch (LionServiceException e) {
				throw e;
			} catch (Exception e) {
				throw new LionServiceException("NettyChannel failed to connect to server, url: " + endpoint.getUrl().getUri(), e);
			} finally {
				if (!state.isAliveState()) {
					endpoint.incrErrorCount();
					if(channelFuture.channel() != null){
		            	channel.close();
		            }
				}
			}
		}
		else {
			state = ChannelState.ALIVE;
			LoggerUtil.debug("NettyChannel server connect: " + endpoint.getUrl().getUri() + " local=" + localAddress);
			
			return true;
		}
	}

	@Override
	public Response request(Request request) throws TransportException {
		int timeout = endpoint.getUrl().getMethodParameter(request.getMethodName(), request.getParamtersDesc(),
	            URLParamType.requestTimeout.getName(), URLParamType.requestTimeout.getIntValue());
		if (timeout <= 0) {
               throw new LionFrameworkException("NettyClient init Error: timeout(" + timeout + ") <= 0 is forbid.", LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
        }
		
		ResponseFuture response = new NettyResponseFuture(request, timeout, this.endpoint);
		this.endpoint.registerCallback(request.getRequestId(), response);
		
		ChannelFuture writeFuture = this.channel.writeAndFlush(request);
		
		boolean result = writeFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
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
			response = this.endpoint.removeCallback(request.getRequestId());

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
		
		ChannelFuture writeFuture = this.channel.writeAndFlush(transportData);
		boolean result = writeFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
		
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
			LoggerUtil.error("NettyChannel close Error: " + endpoint.getUrl().getUri() + " local=" + localAddress, e);
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
