/**
 * 版权声明：bee 版权所有 违者必究 2016
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

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.url.URL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.common.utils.StandardThreadExecutor;
import com.alacoder.lion.remote.AbstractServer;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.ChannelState;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.TransportException;
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

//	private ConcurrentMap<String, io.netty.channel.Channel> channels = new ConcurrentHashMap<String, io.netty.channel.Channel>();
	private io.netty.channel.Channel serverChannel;
	private ConcurrentMap<String, io.netty.channel.Channel> channels = null;

	private ServerBootstrap  server;
	
	EventLoopGroup bossGroup  = null;
	EventLoopGroup workerGroup = null;
	 
	public NettyServer(URL url,MessageHandler messagehandler) {
		super(url,messagehandler);
		this.channels = new ConcurrentHashMap<String, io.netty.channel.Channel>();
	}

	private void init() {
		if(state != ChannelState.UNINIT) {
			LoggerUtil.error("NettyServer is not in uninit state, init error, url = {} ", url.getUri());
			return ;
		}
		
		boolean shareChannel = url.getBooleanParameter(URLParamType.shareChannel.getName(),
				URLParamType.shareChannel.getBooleanValue());
		final int maxContentLength = url.getIntParameter(URLParamType.maxContentLength.getName(),
				URLParamType.maxContentLength.getIntValue());
		final int maxServerConnection = url.getIntParameter(URLParamType.maxServerConnection.getName(),
				URLParamType.maxServerConnection.getIntValue());
		int workerQueueSize = url.getIntParameter(URLParamType.workerQueueSize.getName(),
				URLParamType.workerQueueSize.getIntValue());

		int minWorkerThread = 0, maxWorkerThread = 0;

		if (shareChannel) {
			minWorkerThread = url.getIntParameter(URLParamType.minWorkerThread.getName(),
					LionConstants.NETTY_SHARECHANNEL_MIN_WORKDER);
			maxWorkerThread = url.getIntParameter(URLParamType.maxWorkerThread.getName(),
					LionConstants.NETTY_SHARECHANNEL_MAX_WORKDER);
		} else {
			minWorkerThread = url.getIntParameter(URLParamType.minWorkerThread.getName(),
					LionConstants.NETTY_NOT_SHARECHANNEL_MIN_WORKDER);
			maxWorkerThread = url.getIntParameter(URLParamType.maxWorkerThread.getName(),
					LionConstants.NETTY_NOT_SHARECHANNEL_MAX_WORKDER);
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
         .handler(new NettyServerChannelHandler(maxServerConnection, channels))
         .childHandler(new  ChannelInitializer<SocketChannel>(){
        	 @Override
        	    public void initChannel(SocketChannel ch) throws Exception {
        	        ChannelPipeline pipeline = ch.pipeline();
        	        
        	        pipeline.addLast("decoder", new NettyDecodeHandler(codec,maxContentLength));
        	        pipeline.addLast("encoder", new NettyEncodeHandler(codec));
        	        pipeline.addLast("handler", new NettyServerChildChannelHandler(messagehandler,standardThreadExecutor,channels,maxServerConnection));
        	    }
         })
         .childOption(ChannelOption.TCP_NODELAY, true)
         .childOption(ChannelOption.SO_KEEPALIVE, true);
    	
    	state = ChannelState.INIT;
	}
	
	public boolean open() {
		init() ;
		
		doOpen();
		
		heartbeat();
		
		return true;
	}
	
	private void heartbeat(){
		
		
	}
	
	
	private void doOpen() {
		
		if(state != ChannelState.INIT) {
			LoggerUtil.error("NettyServer is not in init state, open error, url = {} ", url.getUri());
			return ;
		}
		
		try {

			ChannelFuture channelFuture = server.bind(new InetSocketAddress(url.getPort())).sync();
			serverChannel = channelFuture.channel();
			

			state = ChannelState.ALIVE;

			LoggerUtil.info("NettyServer open success , url = {}", url.getUri());

		} catch (InterruptedException e) {
			LoggerUtil.warn("NettyServer close fail: interrupted url = {} ", url.getUri());
			close();
		} catch (Throwable e) {
			LoggerUtil.error("NettyServer error  ", e);
			close();
		}
	}
	
	@Override
	public Collection<Channel> getChannels() {
		throw new LionFrameworkException("NettyServer Collection<Channel> getChannels()  method unsupport: url: " + url);
	}

	@Override
	public Channel getServerChannel() {
		throw new LionFrameworkException("NettyServer Channel getServerChannel()  method unsupport: url: " + url);
	}

	
	@Override
	public void close(int timeout) {
		//判断系统server是否启动
		if (state.isCloseState()) {
			LoggerUtil.info("NettyServer close fail: already close, url={}", url.getUri());
			return;
		}

		if (state.isUnInitState()) {
			LoggerUtil.info("NettyServer close Fail: don't need to close because node is unInit state: url={}", url.getUri());
			return;
		}
		
		//关闭 serverchannel
		try{
			if(serverChannel != null) {
				serverChannel.close();
			}
		} catch(Throwable e) {
			LoggerUtil.info("NettyServer close Fail: close error: url={}", url.getUri());
			LoggerUtil.info("NettyServer serverChannel.close() error", e);
		}
		
		//关闭netty 相关资源
		try{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
		} catch(Throwable e) {
			LoggerUtil.info("NettyServer close Fail: close error: url={}", url.getUri());
			LoggerUtil.info("NettyServer workerGroup.shutdownGracefully() error", e);
		}
		
		//关闭threadpool相关资源
		try{
			standardThreadExecutor.shutdownNow();
		} catch(Throwable e) {
			LoggerUtil.info("NettyServer close Fail: close error: url={}", url.getUri());
			LoggerUtil.info("NettyServer standardThreadExecutor.shutdownNow() error", e);
		}
		
		// 设置close状态
		state = ChannelState.CLOSE;
	}

	@Override
	public void close() {
		close(0);
	}

	@Override
	public void send(TransportData data) {
		throw new LionFrameworkException("NettyServer  void send(TransportData data) method unsupport: url: " + url);
		
	}
	
	@Override
	public URL getUrl() {
		return this.url;
	}

	@Override
	public Response request(Request request) throws TransportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

}

