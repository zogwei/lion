/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyClientTest.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月16日 下午7:55:01
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;

import com.alacoder.lion.common.url.URL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.transport.DefaultRequest;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: NettyClientTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月16日 下午7:55:01
 *
 */

public class NettyClientTest {

	/**
	 * main(这里用一句话描述这个方法的作用)
	 *
	 * @Title: main
	 * @Description: 
	 * @param @param args    设定文件
	 * @return void    返回类型
	 * @throws
	 */

	public static void main(String[] args) {
		URL url = new URL("netty", "10.12.104.6", 4455, "IHello");
		url.addParameter(URLParamType.connectTimeout.getName(), "10000");
		
		NettyClient client = new NettyClient(url,new MessageHandler(){

			@Override
			public Object handle(Object message) {
				if( message instanceof Response) {
					Response response = (Response)message;
					LoggerUtil.info(" client reciver response ： " + response.getId() );
				}
				return null;
			}
			
		});
		
		client.open();
		
		Request request = new DefaultRequest();
		
		for (int i =0 ; i < 3 ; i++){
			request.setId(System.currentTimeMillis());
			client.send(request);
			 try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}

		client.close();

//        try {
//			Thread.sleep(100000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

	}

}
