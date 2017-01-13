/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyDecodeHandler.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月12日 上午10:45:39
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.remote.Codec;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: NettyDecodeHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月12日 上午10:45:39
 *
 */

public class NettyDecodeHandler extends ByteToMessageDecoder {
	
	private final static LogService logger = LogFactory.getLogService(NettyDecodeHandler.class);
	
	private Codec codec = null;
	 
	private int maxContentLength = 0;

	public NettyDecodeHandler(Codec codec, int maxContentLength){
		this.codec = codec;
		this.maxContentLength = maxContentLength;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,List<Object> out) throws Exception {
		
		io.netty.channel.Channel ch = ctx.channel();
		buffer.markReaderIndex();
		if(buffer.readableBytes() < LionConstants.NETTY_HEADER) {
			return ;
		}

		short magic = buffer.readShort();
		if( magic != LionConstants.NETTY_MAGIC_TYPE ) {
			buffer.resetReaderIndex();
			throw new LionFrameworkException("NettyDecoder transport header not support, type: " + magic);
		}
		
		byte messageType = (byte) buffer.readShort();
		long requestId = buffer.readLong();
		
		int dataLength = buffer.readInt();

		// FIXME 如果dataLength过大，可能导致问题
		if (buffer.readableBytes() < dataLength) {
			buffer.resetReaderIndex();
			return ;
		}
		
		if (maxContentLength > 0 && dataLength > maxContentLength) {
			logger.warn(
					"NettyDecoder transport data content length over of limit, size: {}  > {}. remote={} local={}",
					dataLength, maxContentLength, ch.remoteAddress(), ch.localAddress());
			Exception e = new LionServiceException("NettyDecoder transport data content length over of limit, size: " + dataLength + " > " + maxContentLength);

			if (messageType == LionConstants.FLAG_REQUEST) {
				Response response = buildExceptionResponse(requestId, e);
				ch.writeAndFlush(response);
				throw e;
			} else if (messageType == LionConstants.FLAG_RESPONSE) {
				
			} else {
				throw e;
			}
		}
		
		byte[] data = new byte[dataLength];

		buffer.readBytes(data);
		
		try {
			Object ret = codec.decode(data);
			out.add(ret);
		} catch (Exception e) {
			if (messageType == LionConstants.FLAG_REQUEST) {
				Response resonse = buildExceptionResponse(requestId, e);
				ch.write(resonse);
			} else {
				Response resonse = buildExceptionResponse(requestId, e);
				out.add(resonse);
			}
		}
		
		
	}
	
	private Response buildExceptionResponse(long requestId, Exception e) {
		DefaultResponse response = new DefaultResponse();
		response.setId(requestId);
		response.setException(e);
		return response;
	}
	
}
