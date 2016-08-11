/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: DefaultCodecTest.java
 * @Package com.alacoder.lion.remote.codec
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月11日 上午11:12:05
 * @version V1.0
 */

package com.alacoder.lion.remote.codec;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.url.URL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.Codec;
import com.alacoder.lion.remote.Serialization;
import com.alacoder.lion.remote.transport.DefaultRequest;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: DefaultCodecTest
 * @Description: TODO
 * @author jimmy.zhong
 * @date 2016年8月11日 上午11:12:05
 *
 */

public class DefaultCodecTest {

	public static void main(String[] args) throws IOException {
		Request request = new DefaultRequest();
		request.setId(1111L);
		request.setInterfaceName("interfacename");
		request.setMethod("method");
		
        URL url = new URL("netty", "localhost", 18080, "com.weibo.api.motan.procotol.example.IHello");
//        url.addParameter(URLParamType.serialize.getName(), "mockMotan");
//        url.addParameter(URLParamType.requestTimeout.getName(), "2000");
		
		Channel channel = new Channel(){
			
			URL url = null;

			@Override
			public InetSocketAddress getLocalAddress() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InetSocketAddress getRemoteAddress() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void open() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void close() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void close(int timeout) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void send() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public URL getUrl() {
				// TODO Auto-generated method stub
				return url;
			}
			
			public void setUrl(URL url){
				this.url = url;
			}
			
		};
		
		channel.setUrl(url);
		
		Codec codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension("lion");
		byte[] data = codec.encode(channel, request);
		
		request = (Request)codec.decode(channel, data);

		System.out.println("result : " + request );
		
		
		
		Response response = new DefaultResponse();
		response.setId(1111L);
		response.setInterfaceName("interfacename");
		response.setMethod("method");

		
		codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension("lion");
		data = codec.encode(channel, response);
		
		response = (Response)codec.decode(channel, data);
		
		System.out.println("result Response : " + response );
	}

}
