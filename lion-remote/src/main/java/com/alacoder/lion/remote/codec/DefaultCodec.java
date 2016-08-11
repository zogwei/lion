/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: DefaultCodec.java
 * @Package com.alacoder.lion.remote.codec
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午10:08:59
 * @version V1.0
 */

package com.alacoder.lion.remote.codec;

import java.io.IOException;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.ByteUtil;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.Codec;
import com.alacoder.lion.remote.Serialization;
import com.alacoder.lion.remote.transport.DefaultRequest;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: DefaultCodec
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午10:08:59
 *
 */
@SpiMeta(name = "lion")
public class DefaultCodec implements Codec{

	private static final short MAGIC = (short)0xF0F0;
	private static final byte VERSION = RemoteProtocolVersion.VERSION_1.getVersion();
	private static final int VERSION_HEADER_LENGTH = RemoteProtocolVersion.VERSION_1.getHeaderLength();
	
	@Override
	public byte[] encode(Channel channel, Object message) throws IOException {
		if(message instanceof Request) {
			return encodeRequest(channel, (Request)message);
		} else if ( message instanceof Response ) {
			return encodeResponse(channel, (Response)message);
		} else {
			LoggerUtil.warn(" error message type message is : ", message);
		}
		
		return null;
	}

	@Override
	public Object decode(Channel channel, byte[] data) throws IOException { 
		if( data.length < VERSION_HEADER_LENGTH) {
			throw new LionFrameworkException("decode error: format problem", LionErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
        }
		
		//magic
		short magic = ByteUtil.bytes2short(data, 0);
		if( magic != MAGIC ) {
			 throw new LionFrameworkException("decode error: magic error", LionErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
        }
		
		//version
		byte version =  data[2];
		if( version != VERSION) {
			 throw new LionFrameworkException("decode error: version error", LionErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
        }
		
		// type,requestId , body length , body
		byte dataType = data[3];
		long requestId = ByteUtil.bytes2long(data, 4);
		int bodyLength = ByteUtil.bytes2int(data, 12);
		
		if (VERSION_HEADER_LENGTH + bodyLength != data.length) {
            throw new LionFrameworkException("decode error: content length error", LionErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
        }
		
		byte[] body = new byte[bodyLength];
		System.arraycopy(data, VERSION_HEADER_LENGTH , body, 0, bodyLength);
		
		if(dataType == LionConstants.FLAG_REQUEST) {
			return decodeRequest(channel, body ,requestId);
		} else if ( dataType == LionConstants.FLAG_RESPONSE) {
			return decodeResponse(channel, body ,requestId);
		}
		
		return null;
	}
	
	private Request decodeRequest(Channel channel, byte[] body, Long requestId) throws IOException {
		Serialization serialization =
                ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(
                        channel.getUrl().getParameter(URLParamType.serialize.getName(), URLParamType.serialize.getValue()));
		
		return serialization.deserialize(body, DefaultRequest.class);
	}
	
	private Response decodeResponse(Channel channel, byte[] body, Long requestId) throws IOException {
		Serialization serialization =
                ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(
                        channel.getUrl().getParameter(URLParamType.serialize.getName(), URLParamType.serialize.getValue()));
		
		return serialization.deserialize(body, DefaultResponse.class);
	}
	
	
	private byte[] encodeResponse(Channel channel,Response response) throws IOException {
		Serialization serialization =
                ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(
                        channel.getUrl().getParameter(URLParamType.serialize.getName(), URLParamType.serialize.getValue()));
		
		byte[] body = serialization.serialize(response);
		byte dataType = LionConstants.FLAG_RESPONSE;
		
		return encode(body,dataType , response.getId());
		
	}
	
	
	private byte[] encodeRequest(Channel channel,Request request) throws IOException {
		Serialization serialization =
                ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(
                        channel.getUrl().getParameter(URLParamType.serialize.getName(), URLParamType.serialize.getValue()));
		byte[] body = serialization.serialize(request);
		
		byte dataType = LionConstants.FLAG_REQUEST;
		
		return encode(body,dataType , request.getId());
		
	}
	
	/**
     * 数据协议：
     * 
     * <pre>
	 * 
	 * header:  16个字节 
	 * 
	 * 0-15 bit 	:  magic
	 * 16-23 bit	:  version
	 * 24-31 bit	:  extend flag , 其中： 29-30 bit: event 可支持4种event，比如normal, exception等,  31 bit : 0 is request , 1 is response 
	 * 32-95 bit 	:  request id
	 * 96-127 bit 	:  body content length
	 * 
	 * </pre>
     *
     * @param body
     * @param flag
     * @param requestId
     * @return
     * @throws IOException
     */
	private byte[] encode(byte[] body, byte dataType, Long id) {
		byte[] header = new byte[VERSION_HEADER_LENGTH];
		byte[] data = null;
		int offset = 0;
		
		//magic
		ByteUtil.short2bytes(MAGIC, header, offset);
		offset +=2;
		
		//version
		header[offset] = VERSION;
		offset +=1;
		
		//extend flag 
		header[offset] = dataType;
		offset +=1;
		
		//request id
		ByteUtil.long2bytes(id, header, offset);
		offset +=8;
		
		//body content length
		int bodyLength = body.length;
		ByteUtil.int2bytes(bodyLength, header, offset);
		
		data = new byte[header.length + bodyLength];
		
		System.arraycopy(header, 0, data, 0, header.length);
		System.arraycopy(body, 0, data, header.length, bodyLength);
		
		return data;
	}

}
