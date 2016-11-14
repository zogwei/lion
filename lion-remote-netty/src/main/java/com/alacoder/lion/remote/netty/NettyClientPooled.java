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


import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool.BasePoolableObjectFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import com.alacoder.common.exception.LionAbstractException;
import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.common.utils.StandardThreadExecutor;
import com.alacoder.lion.remote.AbstractPoolClient;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.EndpointState;
import com.alacoder.lion.remote.MessageHandler;
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

public class NettyClientPooled extends AbstractPoolClient {

//	private Channel clientChannel;
	private io.netty.bootstrap.Bootstrap client;
	private EventLoopGroup group;
	
	// 连接到服务器的所有channel，key = remoteIp:remotePort-localIp:localPort作为连接的唯一标示
	private ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<String, Channel>();
	protected StandardThreadExecutor standardThreadExecutor = null;
	
	public NettyClientPooled(LionURL url,MessageHandler messageHandler) {
		super(url,messageHandler);
	}
	
	@Override
	public synchronized boolean open() {
		//初始化client
		init();
		
		// 初始化连接池
		initPool();
		
		return true;
	}
	
	public synchronized Channel getChannel() {
		init();
		return doOpen();
	}
	
	private synchronized void init() {
		if(!state.isUnInitState()){
			return ;
		}
		int workerQueueSize = url.getIntParameter(URLParamType.workerQueueSize.getName(),
				URLParamType.workerQueueSize.getIntValue());

		int minClientWorkerThread = 0, maxClientWorkerThread = 0;
		minClientWorkerThread = url.getIntParameter(URLParamType.minClientWorkerThread.getName(),
				LionConstants.NETTY_CLIENT_MIN_WORKDER);
		maxClientWorkerThread = url.getIntParameter(URLParamType.maxClientWorkerThread.getName(),
				LionConstants.NETTY_CLIENT_MAX_WORKDER);
		
		standardThreadExecutor = (standardThreadExecutor != null && !standardThreadExecutor.isShutdown()) ? standardThreadExecutor
				: new StandardThreadExecutor(minClientWorkerThread, maxClientWorkerThread, workerQueueSize,
						new DefaultThreadFactory("Nettyclient-" + url.getServerPortStr(), true));
		standardThreadExecutor.prestartAllCoreThreads();
		
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
         	         
         	        NettyChannelHandler nettyChannelHandler = new NettyChannelHandler(standardThreadExecutor, channels , NettyClientPooled.this);
         	        
         	         if(messageHandler != null) {
         	        	nettyChannelHandler.setMessagehandler(messageHandler);
         	        	pipeline.addLast("handler", nettyChannelHandler);
         	         }
         	         else {
         	        	pipeline.addLast("handler",nettyChannelHandler); 
         	         }
                 }
             });
		this.state = EndpointState.INIT;
	}
	
	private synchronized Channel doOpen() {
		Channel clientChannel;
		ChannelFuture channelFuture = null;
		io.netty.channel.Channel nettychannel = null;
		try{
			channelFuture = client.connect(new InetSocketAddress(url.getHost(), url.getPort()));
			
			long start = System.currentTimeMillis();
			
			int timeout = url.getIntParameter(URLParamType.connectTimeout.getName(), URLParamType.connectTimeout.getIntValue());
			if (timeout <= 0) {
	            throw new LionFrameworkException("NettyClient init Error: timeout(" + timeout + ") <= 0 is forbid.",
	                    LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
			}
			
			boolean result = channelFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
			boolean success = channelFuture.isSuccess();
			
			if (result && success) {
				nettychannel = channelFuture.channel();
				if (nettychannel.localAddress() != null && nettychannel.localAddress() instanceof InetSocketAddress) {
//					localAddress = (InetSocketAddress) nettychannel.localAddress();
					this.remoteAddress =  new InetSocketAddress(url.getHost(), url.getPort());
				}

				clientChannel = new NettyChannel(this, nettychannel);
				clientChannel.open();
				this.state = EndpointState.ALIVE;
			}
			else {
				boolean connected = false;
	            if(channelFuture.channel() != null){
	            	nettychannel = channelFuture.channel();
	                connected = channelFuture.channel().isActive();
	            }
	            throw new LionServiceException("NettyChannel connect to server timeout url: "
                        + url.getUri() + ", cost: " + (System.currentTimeMillis() - start) + ", result: " + result + ", success: " + success + ", connected: " + connected);
			}
		} catch (LionServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new LionServiceException("NettyChannel failed to connect to server, url: " + url.getUri(), e);
		} finally {
			if (!state.isAliveState() && channelFuture.channel() != null) {
				nettychannel.close();
			}
		}
	    return clientChannel;
	}
	
	
	@Override
	public Response request(Request request) throws TransportException {
		Channel channel = null;
			// return channel or throw exception(timeout or connection_fail)
		try {
			channel = borrowObject();
			if(!channel.isAvailable()){
				throw new LionServiceException("NettyChannel is unavaliable: url= " + url.getUri() + " request= " + request);
			}
			
			boolean async = url.getMethodParameter(request.getMethodName(), request.getParamtersDesc()
			        , URLParamType.async.getName(), URLParamType.async.getBooleanValue());
			
			Response response =  request(request,async,channel);
			
			returnObject(channel);
			
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			//TODO 对特定的异常回收channel
			invalidateObject(channel);
		}
		
		return null;

	}
	
	public Response request(Request request, boolean async, Channel channel) throws TransportException {
		Response response = null;
		try {
			response = channel.request(request);
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
		Channel channel = null;
		try{
			channel = borrowObject();
			
			result = channel.send(transportData);
			
			returnObject(channel);
			return result;
		}
		catch(Exception e) {
			//TODO 对特定的异常回收channel
			invalidateObject(channel);
			
			LoggerUtil.error("NettyClient send Error: url=" + url.getUri() + " " + transportData, e);
			if (e instanceof LionAbstractException) {
				throw (LionAbstractException) e;
			} else {
				throw new LionServiceException("NettyClient send Error: url=" + url.getUri() + " "+ transportData, e);
			}
		}
	}
	
	@Override
	public synchronized void close() {
		close(0);
	}

	@Override
	public synchronized void close(int timeout) {
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
			
			super.close();
			
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

	@SuppressWarnings("rawtypes")
	@Override
	protected BasePoolableObjectFactory createChannelFactory() {
		return new NettyClientChannelFactory(this);
	}
}
