/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: Hessian2SerializationTest.java
 * @Package com.alacoder.lion.remote.serialization
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 下午5:59:01
 * @version V1.0
 */

package com.alacoder.lion.remote.serialization;

import java.io.IOException;

import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.remote.Codec;
import com.alacoder.lion.remote.Serialization;
import com.alacoder.lion.remote.transport.DefaultRequest;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: Hessian2SerializationTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 下午5:59:01
 *
 */

public class FastJsonSerializationTest {

	/**
	 * @throws IOException 
	 * main(这里用一句话描述这个方法的作用)
	 *
	 * @Title: main
	 * @Description: 
	 * @param @param args    设定文件
	 * @return void    返回类型
	 * @throws
	 */

	public static void main(String[] args) throws IOException {
		Serialization ser = ExtensionLoader.getExtensionLoader(Serialization.class).getExtension("fastJson");
		Request request = new DefaultRequest();
		request.setRequestId(1111L);
		request.setInterfaceName("interfacename");
		request.setMethodName("method");
		
		byte[] bytes = ser.serialize(request);
		
		request = ser.deserialize(bytes, DefaultRequest.class);
		
		System.out.println("result : " + request);
		
		
		
		Response response = new DefaultResponse();
		response.setRequestId(1111L);
//		response.setInterfaceName("interfacename");
//		response.setMethod("method");

		
		 bytes = ser.serialize(response);
		
		response = ser.deserialize(bytes, DefaultResponse.class);
		
		System.out.println("result Response : " + response );

	}

}
