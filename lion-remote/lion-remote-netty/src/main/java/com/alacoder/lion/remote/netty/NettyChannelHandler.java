/**
 * 版权声明：lion 版权所有 违者必究 2016
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
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.Endpoint;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.ResponseFuture;
import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.TransportException;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName: NettyChannelHandler
 * @Description:
 * @author jimmy.zhong
 * @date 2016年8月12日 下午1:48:35
 *
 */

public class NettyChannelHandler extends SimpleChannelInboundHandler<TransportData> {
	
	private final static LogService logger = LogFactory.getLogService(NettyChannelHandler.class);

	private MessageHandler messagehandler;
	private ThreadPoolExecutor threadPoolExecutor;
	private ConcurrentMap<String, Channel> channels = null;
	private int MaxChannelNum = 1;
	private Endpoint endpoint;

	public NettyChannelHandler(MessageHandler messagehandler,
			ThreadPoolExecutor executor,
			ConcurrentMap<String, Channel> channels,
			int MaxChannelNum,
			Endpoint endpoint) {
		this.messagehandler = messagehandler;
		this.threadPoolExecutor = executor;
		this.channels = channels;
		this.MaxChannelNum = MaxChannelNum;
		this.endpoint = endpoint;
	}
	
	public NettyChannelHandler(
			ThreadPoolExecutor executor,
			ConcurrentMap<String, Channel> channels,
			int MaxChannelNum,
			Endpoint endpoint) {
		this.threadPoolExecutor = executor;
		this.channels = channels;
		this.MaxChannelNum = MaxChannelNum;
		this.endpoint = endpoint;
	}
	
	public NettyChannelHandler(
			ThreadPoolExecutor executor,
			ConcurrentMap<String, Channel> channels,
			Endpoint endpoint) {
		this.threadPoolExecutor = executor;
		this.channels = channels;
		this.endpoint = endpoint;
	}
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("chanel active " + ctx.channel().toString());
		io.netty.channel.Channel channel = ctx.channel();
		String channelKey = getChannelKey(
				(InetSocketAddress) channel.localAddress(),
				(InetSocketAddress) channel.remoteAddress());
		Channel nettyChannel = new NettyChannel(endpoint, channel);
		nettyChannel.open();

		if (channels.size() > MaxChannelNum) {
			// 超过最大连接数限制，直接close连接
			logger.warn("Netty channelConnected channel size out of limit: limit={} current={}",
							MaxChannelNum, channels.size());
			channel.close();
		} else {
			channels.put(channelKey, nettyChannel);
			logger.debug("chanel add  , socket = {}, size = {} ", ctx.channel().toString(), channels.size());
		}
		ctx.fireChannelActive();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TransportData msg) throws Exception {
		io.netty.channel.Channel channel = ctx.channel();
		Channel nettyChannel = new NettyChannel(endpoint, channel);
		nettyChannel.open();
		if (msg instanceof Request) {
			processRequest(nettyChannel, (Request) msg);
		} else if (msg instanceof Response) {
			processResponse(nettyChannel, (Response) msg);
		}
		else {
			// TODO 支持 其他类型 例如：event
		}
	}

	//TODO 关闭连接是没有 日志
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("chanel inactive " + ctx.channel().toString());
		removeChannel(ctx);
		ctx.fireChannelInactive();
	}

	private void removeChannel(ChannelHandlerContext ctx) {
		io.netty.channel.Channel channel = ctx.channel();
		String channelKey = getChannelKey(
				(InetSocketAddress) channel.localAddress(),
				(InetSocketAddress) channel.remoteAddress());

		channels.remove(channelKey);
		logger.debug("chanel remove  " + channelKey);
	}

	private void processResponse(Channel channel, final Response response) {
		if(messagehandler != null) {
			messagehandler.handle(channel, response);
		}
		
		ResponseFuture responseFuture = endpoint.removeCallback(response.getRequestId());

		if (responseFuture == null) {
			logger.warn("NettyClient has response from server, but responseFuture not exist,  requestId={}", response.getRequestId());
		}

		if (response.getException() != null) {
			responseFuture.onFailure(response);
		} else {
			responseFuture.onSuccess(response);
		}
	}

	private void processRequest(final Channel nettyChannel, final Request request) {
		final long processStartTime = System.currentTimeMillis();
		logger.debug("processRequest , request = {} ", request);
		if(messagehandler == null) {
			logger.warn("messagehandler is null ");
			return ;
		}

		// 使用线程池方式处理
		try {
			threadPoolExecutor.execute(new Runnable() {
				@Override
				public void run() {
					//TODO 如果messagehandler.handler 处理时间非常长，会导致线程阻塞 
					Object result = messagehandler.handle(nettyChannel, request);
					DefaultResponse response = null;

					if (!(result instanceof DefaultResponse)) {
						response = new DefaultResponse(result);
					} else {
						response = (DefaultResponse) result;
					}

					response.setRequestId(request.getRequestId());
					response.setProcessTime(System.currentTimeMillis() - processStartTime);

					if (nettyChannel.isAvailable()) {
						try {
							logger.debug("processRequest , response = {} ", response);
							nettyChannel.send(response);
						} catch (Exception e) {
							response = new DefaultResponse();
							response.setRequestId(request.getRequestId());
							response.setException(e);
							response.setProcessTime(System.currentTimeMillis() - processStartTime);
						}
					}
					else {
						logger.error("processRequest error, channel is not available , response = {} ", response);
					}
				}
			});
		} catch (RejectedExecutionException rejectException) {
			DefaultResponse response = new DefaultResponse();
			response.setRequestId(request.getRequestId());
			response.setException(new LionServiceException("process thread pool is full, reject", LionErrorMsgConstant.SERVICE_REJECT));
			response.setProcessTime(System.currentTimeMillis() - processStartTime);
			if (nettyChannel.isAvailable()) {
				try {
					logger.debug("processRequest , response = {} ",	response);
					nettyChannel.send(response);
				} catch (TransportException e) {
					response = new DefaultResponse();
					response.setRequestId(request.getRequestId());
					response.setException(new LionServiceException("send error, "));
					response.setProcessTime(System.currentTimeMillis() - processStartTime);
				} catch (Exception e) {
					response = new DefaultResponse();
					response.setRequestId(request.getRequestId());
					response.setException(e);
					response.setProcessTime(System.currentTimeMillis() - processStartTime);
				}
			}
			else {
				logger.error("processRequest error, channel is not available , response = {} ", response);
			}

			logger.debug("process thread pool is full, reject, active={} poolSize={} corePoolSize={} maxPoolSize={} taskCount={} requestId={}",
							threadPoolExecutor.getActiveCount(),
							threadPoolExecutor.getPoolSize(),
							threadPoolExecutor.getCorePoolSize(),
							threadPoolExecutor.getMaximumPoolSize(),
							threadPoolExecutor.getTaskCount(),
							request.getRequestId());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.warn("netty error", cause);
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
	public static String getChannelKey(InetSocketAddress local,InetSocketAddress remote) {
		String key = "";
		if (local == null || local.getAddress() == null) {
			key += "null-";
		} else {
			key += local.getAddress().getHostAddress() + ":" + local.getPort()
					+ "-";
		}

		if (remote == null || remote.getAddress() == null) {
			key += "null";
		} else {
			key += remote.getAddress().getHostAddress() + ":"
					+ remote.getPort();
		}

		return key;
	}

	public Map<String, Channel> getChannels() {
		return channels;
	}

	public MessageHandler getMessagehandler() {
		return messagehandler;
	}

	public void setMessagehandler(MessageHandler messagehandler) {
		this.messagehandler = messagehandler;
	}

	public int getMaxChannelNum() {
		return MaxChannelNum;
	}

	public void setMaxChannelNum(int maxChannelNum) {
		MaxChannelNum = maxChannelNum;
	}

}
