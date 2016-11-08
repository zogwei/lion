/**
 * 版权声明：lion 版权所有 违者必究 2016
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

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

public class NettyClientTest  extends TestCase {

    @Test
	public void testClientSend() throws TransportException {
		LionURL url = new LionURL("", "10.12.104.6", 4455, "");
		url.addParameter(URLParamType.connectTimeout.getName(), "10000");
		url.addParameter(URLParamType.requestTimeout.getName(), "10000");
		Request request = new DefaultRequest();
		NettyClient client = null;

		LoggerUtil.info(" client send() begin ： ");
		client = new NettyClient(url, new MessageHandler() {
			@Override
			public Object handle(Channel channel, Object message) {
				if (message instanceof Response) {
					Response response = (Response) message;
					LoggerUtil.info(" client reciver send response ： " + response.getRequestId());
				}
				return null;
			}
		});

		client.open();

		for (int i = 0; i < 3; i++) {
			request.setRequestId(System.currentTimeMillis());
			client.send(request);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		client.close();
	}
    
    @Test
   	public void testClientRequest() throws TransportException {
   		LionURL url = new LionURL("", "10.12.104.6", 4455, "");
   		url.addParameter(URLParamType.connectTimeout.getName(), "10000");
   		Request request = new DefaultRequest();
   		NettyClient client = null;
   		
   		LoggerUtil.info(" client request() begin ： " );
   		 client = new NettyClient(url,null);
   	     client.open();
   		
		for (int i = 0; i < 1; i++) {
			request.setRequestId(System.currentTimeMillis());
			Response response = client.request(request);
			LoggerUtil.info(" client reciver request() response ： "
					+ response.getRequestId() + " value: "
					+ response.getValue());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

   		client.close();
   	}

}
