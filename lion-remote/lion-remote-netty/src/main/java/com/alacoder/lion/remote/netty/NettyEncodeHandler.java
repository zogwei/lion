/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyEncodeHandler.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月12日 上午10:46:08
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;

import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.utils.ByteUtil;
import com.alacoder.lion.remote.Codec;
import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName: NettyEncodeHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月12日 上午10:46:08
 *
 */

public class NettyEncodeHandler extends MessageToByteEncoder<TransportData> {
	
	private Codec codec = null;
	
	public NettyEncodeHandler(Codec codec){
		this.codec = codec;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, TransportData msg, ByteBuf out) throws Exception {
		byte[] data = null;
		Long id = null;
		id =  ((TransportData)msg).getId();
		if(msg instanceof Response) {
			try{
				data = codec.encode(msg);
			} catch (Exception e) {
				Response<?> response = buildExceptionResponse(id, e);
				data = codec.encode(response);
			}
		} else {
			data = codec.encode(msg);
		} 
		
		byte[] transportHeader = new byte[LionConstants.NETTY_HEADER];
		ByteUtil.short2bytes(LionConstants.NETTY_MAGIC_TYPE, transportHeader, 0);
		transportHeader[3] = getType(msg);
		ByteUtil.long2bytes(id, transportHeader, 4);
		ByteUtil.int2bytes(data.length, transportHeader, 12);
		
		out.writeBytes(transportHeader);
		out.writeBytes(data);
	}
	
	private byte getType(Object message) {
		if (message instanceof Request) {
			return LionConstants.FLAG_REQUEST;
		} else if (message instanceof Response) {
			return LionConstants.FLAG_RESPONSE;
		} else {
			return LionConstants.FLAG_OTHER;
		}
	}

	private Response<?> buildExceptionResponse(long requestId, Exception e) {
		@SuppressWarnings("rawtypes")
		DefaultResponse response = new DefaultResponse();
		response.setId(requestId);
		response.setException(e);
		return response;
	}
}
