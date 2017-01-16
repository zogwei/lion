/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyServerTest.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月16日 下午7:54:49
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;

import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.MessageHandlerAdpter;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;

/**
 * @ClassName: NettyServerTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月16日 下午7:54:49
 *
 */

public class NettyServerTest {

	private final static LogService logger = LogFactory.getLogService(NettyServerTest.class);
	
	public static void main(String[] args) {
//		LionURL url = new LionURL("protcol", "host", 4455, "path");
		LionURL url = new LionURL("", "", 4455, "path");
		
		NettyServer server = new NettyServer(url,new MessageHandlerAdpter(){

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public DefaultResponse handle(Channel channel, Request<?> message) {
					Request<String> request = (Request<String>)message;
					logger.info(" server reciver request ： " + request.getId() +" msg； " + request.getRequestMsg());
					DefaultResponse response = new DefaultResponse();
					response.setId(request.getId());
					response.setRequestId(request.getId());
					response.setValue("response value : " + request.getId() );
					return response;
			}
			
		});
		
		server.open();

        try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        server.close();
	}

}
