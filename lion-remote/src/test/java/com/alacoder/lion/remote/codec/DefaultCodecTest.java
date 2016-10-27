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
import com.alacoder.lion.common.url.LionURL;
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
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月11日 上午11:12:05
 *
 */

public class DefaultCodecTest {

	public static void main(String[] args) throws IOException {
		Request request = new DefaultRequest();
		request.setRequestId(1111L);
		request.setInterfaceName("interfacename");
		request.setMethodName("method");
		
        LionURL url = new LionURL("netty", "localhost", 18080, "com.alacoder.api.lion.procotol.example.IHello");
//        url.addParameter(URLParamType.serialize.getName(), "mockLion");
//        url.addParameter(URLParamType.requestTimeout.getName(), "2000");
		

		Codec codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension("lion");
		byte[] data = codec.encode( request);
		
		request = (Request)codec.decode(data);

		System.out.println("result : " + request );
		
		
		
		Response response = new DefaultResponse();
		response.setRequestId(1111L);
//		response.setInterfaceName("interfacename");
//		response.setMethod("method");

		
		codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension("lion");
		data = codec.encode(response);
		
		response = (Response)codec.decode(data);
		
		System.out.println("result Response : " + response );
	}

}
