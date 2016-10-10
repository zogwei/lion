/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyChannelHandler.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月12日 下午1:48:35
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @ClassName: NettyChannelHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月12日 下午1:48:35
 *
 */

public class NettyServerChildChannelHandler extends SimpleChannelInboundHandler<TransportData> {
	
	private MessageHandler messagehandler;
	private ThreadPoolExecutor threadPoolExecutor;
	private ConcurrentMap<String, io.netty.channel.Channel> channels = null;
	private int MaxChannelNum = 0;
	
	public NettyServerChildChannelHandler(MessageHandler messagehandler,ThreadPoolExecutor executor,ConcurrentMap<String, io.netty.channel.Channel> channels,int MaxChannelNum) {
		this.messagehandler = messagehandler;
		this.threadPoolExecutor = executor;
		this.channels = channels;
		this.MaxChannelNum = MaxChannelNum;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TransportData msg) throws Exception {
		io.netty.channel.Channel channel = ctx.channel();
		if( msg instanceof Request ) {
			processRequest((Request) msg,channel);
		} else if ( msg instanceof Response ) {
			processResponse((Response) msg,channel);
		}
	}
	
	private void removeChannel(ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		String channelKey = getChannelKey(
				(InetSocketAddress) channel.localAddress(),
				(InetSocketAddress) channel.remoteAddress());

		channels.remove(channelKey);
		LoggerUtil.debug("chanel remove  " + channelKey);
	}	
	
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	LoggerUtil.debug("chanel inactive " + ctx.channel().toString());
    	removeChannel(ctx);
        ctx.fireChannelInactive();
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	LoggerUtil.info("chanel active " + ctx.channel().toString());
    	NioSocketChannel nioChannel = (NioSocketChannel) ctx.channel();
    	
    	String channelKey = getChannelKey((InetSocketAddress) nioChannel.localAddress(), (InetSocketAddress) nioChannel.remoteAddress());

		if (channels.size() > MaxChannelNum) {
			// 超过最大连接数限制，直接close连接
			LoggerUtil.warn("NettyServerChannelManage channelConnected channel size out of limit: limit={} current={}",
					MaxChannelNum, channels.size());

			nioChannel.close();
		} else {
			channels.put(channelKey, nioChannel);
			LoggerUtil.debug("chanel add  , socket = {}, size = {} " , ctx.channel().toString(),channels.size());
		}
        ctx.fireChannelActive();
    }
	
	private void processResponse(final Response response, final io.netty.channel.Channel channel) {
		messagehandler.handle(response);
	}
	
		
	private void processRequest(final Request request, final io.netty.channel.Channel channel) {
		final long processStartTime = System.currentTimeMillis();

		// 使用线程池方式处理
		try {
			threadPoolExecutor.execute(new Runnable() {
				@Override
                public void run() {
                    Object result = messagehandler.handle(request);
                    DefaultResponse response = null;

            		if (!(result instanceof DefaultResponse)) {
            			response = new DefaultResponse(result);
            		} else {
            			response = (DefaultResponse) result;
            		}
            		
            		response.setRequestId(request.getRequestId());
            		response.setProcessTime(System.currentTimeMillis() - processStartTime);

            		if (channel.isActive()) {
            			channel.writeAndFlush(response);
            		}
                }
            });
		} catch (RejectedExecutionException rejectException) {
			DefaultResponse response = new DefaultResponse();
			response.setRequestId(request.getRequestId());
			response.setException(new LionServiceException("process thread pool is full, reject", LionErrorMsgConstant.SERVICE_REJECT));
			response.setProcessTime(System.currentTimeMillis() - processStartTime);
			channel.write(response);

			LoggerUtil.debug("process thread pool is full, reject, active={} poolSize={} corePoolSize={} maxPoolSize={} taskCount={} requestId={}",
							threadPoolExecutor.getActiveCount(), threadPoolExecutor.getPoolSize(),
							threadPoolExecutor.getCorePoolSize(), threadPoolExecutor.getMaximumPoolSize(),
							threadPoolExecutor.getTaskCount(), request.getRequestId());
		}
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	LoggerUtil.warn("netty error" ,cause);
    	removeChannel(ctx);
        ctx.close();
    }
    
	/**
	 * remote address + local address 作为连接的唯一标示
	 * 
	 * @param local
	 * @param remote
	 * @return
	 */
	private String getChannelKey(InetSocketAddress local, InetSocketAddress remote) {
		String key = "";
		if (local == null || local.getAddress() == null) {
			key += "null-";
		} else {
			key += local.getAddress().getHostAddress() + ":" + local.getPort() + "-";
		}

		if (remote == null || remote.getAddress() == null) {
			key += "null";
		} else {
			key += remote.getAddress().getHostAddress() + ":" + remote.getPort();
		}

		return key;
	}
	
	public Map<String, Channel> getChannels() {
		return channels;
	}

}
