/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyClient.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月16日 下午3:53:17
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.alacoder.common.exception.LionAbstractException;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.AbstractClient;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.EndpointState;
import com.alacoder.lion.remote.Endpoint;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.ResponseFuture;
import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.TransportException;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: NettyClient
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月16日 下午3:53:17
 *
 *TODO heartbeat支持 、客户端重连、JMX
 */

public class NettyPooledClient extends AbstractClient {

	private Channel clientChannel;
	private io.netty.bootstrap.Bootstrap client;
	private EventLoopGroup group;
	
	public NettyPooledClient(LionURL url,MessageHandler messageHandler) {
		super(url,messageHandler);
		this.maxClientConnection = 1;
	}
	
	@Override
	public boolean open() {
		final int maxContentLength = url.getIntParameter(URLParamType.maxContentLength.getName(), URLParamType.maxContentLength.getIntValue());
		
		group = new NioEventLoopGroup();
		client = new Bootstrap();
		client.group(group)
		      .channel(NioSocketChannel.class)
		      .option(ChannelOption.TCP_NODELAY, true)
		      .option(ChannelOption.SO_KEEPALIVE, true)
		      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
		      .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                	 ChannelPipeline pipeline = ch.pipeline();
                	 pipeline.addLast("decoder", new NettyDecodeHandler(codec,maxContentLength));
         	         pipeline.addLast("encoder", new NettyEncodeHandler(codec));
         	        
         	         if(messageHandler != null) {
//         	        	pipeline.addLast("handler", new NettyClientChannelHandler( NettyPooledClient.this,  messageHandler));
         	         }
         	         else {
//         	        	pipeline.addLast("handler", new NettyClientChannelHandler( NettyPooledClient.this, new NettyMessageHandler(NettyPooledClient.this))); 
         	         }
                 }
             });

		try {
			NettyChannel nettyChannel = new NettyChannel(this);
			nettyChannel.open();
			clientChannel = nettyChannel;
			this.state = EndpointState.ALIVE;
			return state.isAliveState();
		} catch (Exception e) {
			LoggerUtil.error("NettyClient open fail:  url =  "+ url.getUri(), e);
			throw new LionServiceException("NettyClient failed to open , url: "+ getUrl().getUri(), e);
		} finally {
			if(!clientChannel.isAvailable()) {
				clientChannel.close();
			}
		}
	}
	
	@Override
	public Response request(Request request) throws TransportException {
		if(!clientChannel.isAvailable()){
			throw new LionServiceException("NettyChannel is unavaliable: url= " + url.getUri() + " request= " + request);
		}
		
		boolean async = url.getMethodParameter(request.getMethodName(), request.getParamtersDesc()
		        , URLParamType.async.getName(), URLParamType.async.getBooleanValue());
		
		return request(request,async);
	}
	
	public Response request(Request request, boolean async) throws TransportException {
		Response response = null;
		try {
			response = clientChannel.request(request);
		} catch (Exception e) {
			LoggerUtil.error("NettyClient request Error: url=" + url.getUri()
					+ " " + request, e);
			if (e instanceof LionAbstractException) {
				throw (LionAbstractException) e;
			} else {
				throw new LionServiceException(
						"NettyClient request Error: url=" + url.getUri() + " "
								+ request, e);
			}
		}

		if (async || !(response instanceof NettyResponseFuture)) {
			return response;
		}

		return new DefaultResponse(response);
	}
	
	@Override
	public boolean send(TransportData transportData) throws TransportException {
		boolean result = false;
		try{
			result = clientChannel.send(transportData);
			return result;
		}
		catch(Exception e) {
			LoggerUtil.error("NettyClient send Error: url=" + url.getUri() + " " + transportData, e);
			if (e instanceof LionAbstractException) {
				throw (LionAbstractException) e;
			} else {
				throw new LionServiceException("NettyClient send Error: url=" + url.getUri() + " "+ transportData, e);
			}
		}
	}
	
	@Override
	public void close() {
		close(0);
	}

	@Override
	public void close(int timeout) {
		if(state.isCloseState()){
			LoggerUtil.info("NettyClient close fail: already close, url={}", url.getUri());
			return;
		}
		
		// 如果当前nettyClient还没有初始化，那么就没有close的理由。
		if (state.isUnInitState()) {
			LoggerUtil.info("NettyClient close Fail: don't need to close because node is unInit state: url={}", url.getUri());
			return;
		}
		
		try{
			timeMonitorFuture.cancel(true);
			
			// 清空callback
			callbackMap.clear();
			
			clientChannel.close();
			
			group.shutdownGracefully();
			
			// 设置close状态
			state = EndpointState.CLOSE;
			LoggerUtil.info("NettyClient close :  url = {} ", url.getUri());
		}
		catch(Exception e) {
			LoggerUtil.error("NettyClient close Error: url=" + url.getUri(), e);
		}
	}
	


	@Override
	public LionURL getUrl() {
		return url;
	}

	@Override
	public boolean isAvailable() {
		return state.isAliveState();
	}

	@Override
	public boolean isClosed() {
		return state.isCloseState();
	}
	
	public io.netty.bootstrap.Bootstrap getClient() {
		return client;
	}
	
}
