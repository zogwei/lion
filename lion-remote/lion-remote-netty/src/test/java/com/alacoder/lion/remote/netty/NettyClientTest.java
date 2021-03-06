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

import junit.framework.TestCase;

import org.junit.Test;

import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.MessageHandlerAdpter;
import com.alacoder.lion.remote.TransportException;
import com.alacoder.lion.remote.transport.DefaultRequest;
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
	
	private final static Log logger = LogFactory.getLog(NettyClientTest.class);

    @Test
	public void testClientSend() throws TransportException {
		LionURL url = new LionURL("", "127.0.0.1", 4455, "path");
		url.addParameter(URLParamType.connectTimeout.getName(), "10000");
		url.addParameter(URLParamType.requestTimeout.getName(), "10000");
		Request<String> request = new DefaultRequest<String>();
		request.setRequestMsg("request value");
		NettyClient client = null; 

		logger.info(" client send() begin ： ");
		client = new NettyClient(url, new MessageHandlerAdpter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object handle(Channel channel, Response message) {
				Response response = (Response) message;
				logger.info(" client reciver send response ： " + response.getRequestId());
				return null;
			}
		});

		client.open();

		for (int i = 0; i < 3; i++) {
			request.setId(System.currentTimeMillis());
			client.send(request);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		client.close();
	}
    
    @SuppressWarnings("rawtypes")
	@Test
   	public void testClientRequest() throws TransportException {
   		LionURL url = new LionURL("", "127.0.0.1", 4455, "");
   		url.addParameter(URLParamType.connectTimeout.getName(), "10000");
		Request<String> request = new DefaultRequest<String>();
		request.setRequestMsg("request value");
   		NettyClient client = null;
   		
   		logger.info(" client request() begin ： " );
   		 client = new NettyClient(url,null);
   	     client.open();
   		
		for (int i = 0; i < 10000; i++) {
			request.setId(System.currentTimeMillis());
			Response response = client.request(request);
			logger.info(" client reciver request() response ： "
					+ response.getRequestId() + " value: "
					+ response.getValue());
			try {
				Thread.sleep(10000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

   		client.close();
   	}

}
