/**
 * 版权声明：bee 版权所有 违者必究 2016
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

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.telnet.TelnetClientInitializer;

import com.alacoder.lion.common.url.URL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.AbstractClient;
import com.alacoder.lion.remote.ChannelState;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.TransportData;

/**
 * @ClassName: NettyClient
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月16日 下午3:53:17
 *
 *TODO 1、客户端池支持
 */

public class NettyClient extends AbstractClient {
	
	private MessageHandler messageHandler;
	private io.netty.channel.Channel clientChannel;
	private io.netty.bootstrap.Bootstrap client;
	private EventLoopGroup group;
	
	public NettyClient(URL url,MessageHandler messageHandler) {
		super(url);
		this.messageHandler = messageHandler;
	}
	
	@Override
	public void doOpen() {
		final int maxContentLength = url.getIntParameter(URLParamType.maxContentLength.getName(),
				URLParamType.maxContentLength.getIntValue());
		
		String host = url.getHost(); 
		int port = url.getPort();
		
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
         	        
         	         pipeline.addLast("handler", new NettyChannelHandler(messageHandler,null));
                 }
             });

		ChannelFuture f  = null;
		try {
			state = ChannelState.INIT;
		    f = client.connect(host, port);
			f.await(connectTimeout, TimeUnit.MILLISECONDS);
			if ( f != null && f.isSuccess() ) {
				clientChannel = f.channel();
				state = ChannelState.ALIVE;
			} else {
				// fail
				LoggerUtil.error("NettyClient open fail:  url = {} ", url.getUri());
				close();
			}
		} catch (InterruptedException e) {
			LoggerUtil.error("NettyClient open fail:  url = {} ", url.getUri());
			LoggerUtil.error("NettyClient error ", e);
			close();
		} finally {
			if(!clientChannel.isActive()) {
				f.cancel(false);
			}
		}
		
	}
	


	@Override
	public void send(TransportData data) {
		if( clientChannel.isActive() ) {
			clientChannel.writeAndFlush(data);
		} else {
			LoggerUtil.warn("NettyClient close fail:  url = {} ", url.getUri());
		}
		
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close(int timeout) {
		// TODO Auto-generated method stub
		
	}

}
