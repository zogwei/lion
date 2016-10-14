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

import java.util.concurrent.RejectedExecutionException;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.TransportData;
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

public class NettyClientChannelHandler extends SimpleChannelInboundHandler<TransportData> {
	
	private MessageHandler messagehandler;
	private Channel channel ;
	
	public NettyClientChannelHandler(Channel nettyChannel , MessageHandler messagehandler) {
		this.channel = nettyChannel; 
		this.messagehandler = messagehandler;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TransportData msg) throws Exception {
		if( msg instanceof Request ) {
			processRequest(channel,(Request) msg,ctx);
		} else if ( msg instanceof Response ) {
			processResponse(channel,(Response) msg);
		}
	}
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	LoggerUtil.debug("chanel inactive " + ctx.channel().toString());
        ctx.fireChannelInactive();
    }
	
	private void processResponse(Channel channel,final Response response) {
		
		messagehandler.handle(channel,response);
	}
	
		
	private void processRequest(Channel channel,final Request request,ChannelHandlerContext ctx) {
		final long processStartTime = System.currentTimeMillis();

		try {
			Object result = messagehandler.handle(channel,request);
            DefaultResponse response = null;

    		if (!(result instanceof DefaultResponse)) {
    			response = new DefaultResponse(result);
    		} else {
    			response = (DefaultResponse) result;
    		}
    		
    		response.setRequestId(request.getRequestId());
    		response.setProcessTime(System.currentTimeMillis() - processStartTime);

    		if (ctx.channel().isActive()) {
    			ctx.channel().write(response);
    		}
		} catch (RejectedExecutionException rejectException) {
			DefaultResponse response = new DefaultResponse();
			response.setRequestId(request.getRequestId());
			response.setException(new LionServiceException("process thread pool is full, reject", LionErrorMsgConstant.SERVICE_REJECT));
			response.setProcessTime(System.currentTimeMillis() - processStartTime);
			if (ctx.channel().isActive()) {
    			ctx.channel().write(response);
    		}
		}
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	LoggerUtil.warn("netty error" ,cause);
        ctx.close();
    }

}
