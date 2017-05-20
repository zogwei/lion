/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyServer.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午4:54:08
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;

import com.alacoder.common.exception.LionAbstractException;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.exception.LionServiceException;
import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.StandardThreadExecutor;
import com.alacoder.lion.remote.AbstractServer;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.ChannelState;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.TransportException;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: NettyServer
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午4:54:08
 *
 */

public class NettyServer extends AbstractServer{
	
	private final static Log logger = LogFactory.getLog(NettyServer.class);

	private Channel serverChannel;
	
	// 连接到服务器的所有channel，key = remoteIp:remotePort-localIp:localPort作为连接的唯一标示
	private ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<String, Channel>();

	private ServerBootstrap  server;
	
	EventLoopGroup bossGroup  = null;
	EventLoopGroup workerGroup = null;
	
	public NettyServer(LionURL url,MessageHandler messagehandler) {
		super(url,messagehandler);
	}
	
	protected synchronized void doOpen() {
		init() ;
		
		doOpen1();
		
	}
	
	protected synchronized void doClose(int timeout) {

		
		//关闭 serverchannel
		try{
			if(serverChannel != null) {
				serverChannel.close();
			}
		} catch(Throwable e) {
			logger.info("NettyServer close Fail: close error: url=" + url.getUri(),e);
		}
		
		//关闭netty 相关资源
		try{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
		} catch(Throwable e) {
			logger.info("NettyServer close Fail: close error: url=" + url.getUri(),e);
		}
	}
	
	private synchronized void init() {
		if(state != ChannelState.UNINIT) {
			logger.error("NettyServer is not in uninit state, init error, url = {} ", url.getUri());
			return ;
		}
		
		boolean shareChannel = url.getBooleanParameter(URLParamType.shareChannel.getName(),URLParamType.shareChannel.getBooleanValue());
		final int maxContentLength = url.getIntParameter(URLParamType.maxContentLength.getName(),URLParamType.maxContentLength.getIntValue());
		final int maxServerConnection = url.getIntParameter(URLParamType.maxServerConnection.getName(),URLParamType.maxServerConnection.getIntValue());
		int workerQueueSize = url.getIntParameter(URLParamType.workerQueueSize.getName(),URLParamType.workerQueueSize.getIntValue());

		int minWorkerThread = 0, maxWorkerThread = 0;

		if (shareChannel) {
			minWorkerThread = url.getIntParameter(URLParamType.minWorkerThread.getName(),LionConstants.NETTY_SHARECHANNEL_MIN_WORKDER);
			maxWorkerThread = url.getIntParameter(URLParamType.maxWorkerThread.getName(),LionConstants.NETTY_SHARECHANNEL_MAX_WORKDER);
		} else {
			minWorkerThread = url.getIntParameter(URLParamType.minWorkerThread.getName(),LionConstants.NETTY_NOT_SHARECHANNEL_MIN_WORKDER);
			maxWorkerThread = url.getIntParameter(URLParamType.maxWorkerThread.getName(),LionConstants.NETTY_NOT_SHARECHANNEL_MAX_WORKDER);
		}
		
		standardThreadExecutor = (standardThreadExecutor != null && !standardThreadExecutor.isShutdown()) ? standardThreadExecutor
				: new StandardThreadExecutor(minWorkerThread, maxWorkerThread, workerQueueSize,
						new DefaultThreadFactory("NettyServer-" + url.getServerPortStr(), true));
		standardThreadExecutor.prestartAllCoreThreads();
		
	    bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        
        server = new ServerBootstrap();
    	server.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
//         .handler(new NettyServerChannelHandler(maxServerConnection, channels))
         .childHandler(new  ChannelInitializer<SocketChannel>(){
        	 @Override
        	    public void initChannel(SocketChannel ch) throws Exception {
        	        ChannelPipeline pipeline = ch.pipeline();
        	        
        	        pipeline.addLast("decoder", new NettyDecodeHandler(codec,maxContentLength));
        	        pipeline.addLast("encoder", new NettyEncodeHandler(codec));
        	        pipeline.addLast("handler", new NettyChannelHandler(messagehandler,standardThreadExecutor,channels,maxServerConnection,NettyServer.this));
        	    }
         })
         .childOption(ChannelOption.TCP_NODELAY, true)
         .childOption(ChannelOption.SO_KEEPALIVE, true);
    	
    	state = ChannelState.INIT;
    	logger.info("NettyServer init success : url={}", url.getUri());
    	
	}
	
	private synchronized void doOpen1() {
		if(state != ChannelState.INIT) {
			logger.error("NettyServer is not in init state, open error, url = {} ", url.getUri());
			return;
		}
		
		ChannelFuture channelFuture = null;
		try {
			InetSocketAddress bindAdd = null;
			String host = url.getHost();
			if(!StringUtils.isEmpty(host)) {
				bindAdd = new InetSocketAddress(host,url.getPort());
			}
			else {
				bindAdd = new InetSocketAddress(url.getPort());
			}
			channelFuture = server.bind(bindAdd).sync();
			if(channelFuture.isSuccess()){
				serverChannel =  new NettyChannel(this, channelFuture.channel());
				serverChannel.open();
				this.remoteAddress = bindAdd;
			}
			else {
				logger.info("NettyServer open bind error , url = {}", url.getUri());
				if(channelFuture.cause() != null) {
					throw new LionServiceException("NettyServer bind fail url: "+ url.getUri(),channelFuture.cause());
				}
				else {
					throw new LionServiceException("NettyServer bind fail url: "+ url.getUri());
				}
			}
		} catch (InterruptedException e) {
			logger.warn("NettyServer close fail: interrupted url = {} ", url.getUri());
			throw new LionServiceException("NettyServer failed to bind , url: "+ getUrl().getUri(), e);
		} catch (Throwable e) {
			logger.error("NettyServer error  ", e);
			throw new LionServiceException("NettyServer failed to bind , url: "+ getUrl().getUri(), e);
		} finally{
			if(!channelFuture.isSuccess()) {
				close() ;
			}
		}
	}
	
	@Override
	public Response<?> request(Request<?> request) throws TransportException {
		throw new LionFrameworkException("NettyServer request(Request request) method unsupport: url: " + url);
	}
	
	@Override
	public boolean send(TransportData transportData) throws TransportException {
		throw new LionFrameworkException("NettyServer send(TransportData transportData) method unsupport: url: " + url);
	}
	
	@Override
	public Response<?> request(Request<?> request, InetSocketAddress clientAdd) throws TransportException {
		String channelKey = NettyChannelHandler.getChannelKey(clientAdd , this.remoteAddress);
		Channel channel = channels.get(channelKey);
		if(channel == null) {
			throw new LionServiceException("NettyChannel is null: url= " + url.getUri() + " request= " + request);
		}
		if(!channel.isAvailable()){
			throw new LionServiceException("NettyChannel is unavaliable: url= " + url.getUri() + " request= " + request);
		}
		
		boolean async = url.getIdentityParameter(request.getIdentity(), URLParamType.async.getName(), URLParamType.async.getBooleanValue());
		
		return request(request,async,channel);
	}

	@Override
	public boolean send(TransportData transportData, InetSocketAddress clientAdd) throws TransportException {
		String channelKey = NettyChannelHandler.getChannelKey(clientAdd , this.remoteAddress);
		Channel channel = channels.get(channelKey);
		if(channel == null) {
			throw new LionServiceException("NettyChannel is null: url= " + url.getUri() + " request= " + transportData);
		}
		if(!channel.isAvailable()){
			throw new LionServiceException("NettyChannel is unavaliable: url= " + url.getUri() + " request= " + transportData);
		}
		
		return send(transportData,channel);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Response<?> request(Request<?> request, boolean async,Channel channel) throws TransportException {
		Response<?> response = null;
		
		try {
			if(channel.isAvailable()){
				response = channel.request(request);
			}
			else {
				logger.error("NettyClient request Error, channel is not availbale: url=" + url.getUri() 
						+ " channel local:  "  + channel.getLocalAddress() 
						+ " channel remote:  "  + channel.getRemoteAddress()
						+ " request " + request);
				throw new LionServiceException("NettyClient request Error, channel is not availbale: url=" + url.getUri() 
						+ " channel local:  "  + channel.getLocalAddress() 
						+ " channel remote:  "  + channel.getRemoteAddress()
						+ " request " + request);
			}
		} catch (LionServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("NettyClient request Error: url=" + url.getUri() + " " + request, e);
			if (e instanceof LionAbstractException) {
				throw (LionAbstractException) e;
			} else {
				throw new LionServiceException("NettyClient request Error: url=" + url.getUri() + " " + request, e);
			}
		}

		if (async || !(response instanceof NettyResponseFuture)) {
			return response;
		}

		//new DefaultResponse(response) 的getvalue 会 syn 结果
		return new DefaultResponse(response);
	}
	
	private  boolean send(TransportData transportData ,Channel channel) throws TransportException {
		boolean result = false;
		try{
			if(channel.isAvailable()){
				channel.send(transportData);
			}
			else {
				logger.error("NettyClient request Error, channel is not availbale: url=" + url.getUri() 
						+ " channel local:  "  + channel.getLocalAddress() 
						+ " channel remote:  "  + channel.getRemoteAddress()
						+ " request " + transportData);
				throw new LionServiceException("NettyClient request Error, channel is not availbale: url=" + url.getUri() 
						+ " channel local:  "  + channel.getLocalAddress() 
						+ " channel remote:  "  + channel.getRemoteAddress()
						+ " request " + transportData);
			}
			result = true;
		} catch (LionServiceException e) {
			throw e;
		}
		catch(Exception e) {
			logger.error("NettyClient send Error: url=" + url.getUri() + " " + transportData, e);
			if (e instanceof LionAbstractException) {
				throw (LionAbstractException) e;
			} else {
				throw new LionServiceException("NettyClient send Error: url=" + url.getUri() + " "+ transportData, e);
			}
		}
		
		return result;
	}
	
	@Override
	public Collection<Channel> getChannels() {
		return channels.values();
	}

	@Override
	public Channel getServerChannel() {
		return serverChannel;
	}
	
	@Override
	public LionURL getUrl() {
		return this.url;
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

