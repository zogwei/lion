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
import java.util.concurrent.ThreadPoolExecutor;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.lion.common.utils.LoggerUtil;
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

public class NettyChannelHandler extends SimpleChannelInboundHandler<TransportData>  {
	
	private MessageHandler messagehandler;
	private ThreadPoolExecutor threadPoolExecutor;
	
	
	public NettyChannelHandler(MessageHandler messagehandler,ThreadPoolExecutor executor) {
		this.messagehandler = messagehandler;
		this.threadPoolExecutor = executor;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TransportData msg) throws Exception {
		io.netty.channel.Channel channel = ctx.channel();
		if( msg instanceof Request ) {
			processRequest((Request) msg,channel);
			return;
		} else if ( msg instanceof Response ) {
			processResponse((Response) msg,channel);
		}
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
            		
            		response.setId(request.getId());
            		response.setProcessTime(System.currentTimeMillis() - processStartTime);

            		if (channel.isActive()) {
            			channel.writeAndFlush(response);
            		}
                }
            });
		} catch (RejectedExecutionException rejectException) {
			DefaultResponse response = new DefaultResponse();
			response.setId(request.getId());
			response.setException(new LionServiceException("process thread pool is full, reject", LionErrorMsgConstant.SERVICE_REJECT));
			response.setProcessTime(System.currentTimeMillis() - processStartTime);
			channel.write(response);

			LoggerUtil.debug("process thread pool is full, reject, active={} poolSize={} corePoolSize={} maxPoolSize={} taskCount={} requestId={}",
							threadPoolExecutor.getActiveCount(), threadPoolExecutor.getPoolSize(),
							threadPoolExecutor.getCorePoolSize(), threadPoolExecutor.getMaximumPoolSize(),
							threadPoolExecutor.getTaskCount(), request.getId());
		}
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	LoggerUtil.error("netty error" ,cause);
        ctx.close();
    }

}
