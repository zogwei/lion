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

import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.TransportException;
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

	public static void main(String[] args) throws TransportException {
		LionURL url = new LionURL("netty", "10.12.104.6", 4455, "IHello");
		url.addParameter(URLParamType.connectTimeout.getName(), "10000");
		
		LoggerUtil.info(" client send() begin ： " );
		NettyClient client = new NettyClient(url,new MessageHandler(){

			@Override
			public Object handle(Channel channel, Object message) {
				if( message instanceof Response) {
					Response response = (Response)message;
					LoggerUtil.info(" client reciver send response ： " + response.getRequestId() );
				}
				return null;
			}
			
		});
		
		client.open();
		
		Request request = new DefaultRequest();
		
		for (int i =0 ; i < 3 ; i++){
			request.setRequestId(System.currentTimeMillis());
			client.send(request);
			 try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		
		client.close();
		
		
		LoggerUtil.info(" client request() begin ： " );
		 client = new NettyClient(url,null);
	     client.open();
		
		for (int i =0 ; i < 3 ; i++){
			request.setRequestId(System.currentTimeMillis());
			Response response = client.request(request);
			LoggerUtil.info(" client reciver request() response ： " + response.getRequestId() + " value: " +response.getValue() );
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
