/**
 * 版权声明：bee 版权所有 违者必究 2016
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

import com.alacoder.lion.common.url.URL;
import com.alacoder.lion.remote.MessageHandler;
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
		URL url = new URL("netty", "127.0.0.1", 4455, "com.weibo.api.motan.procotol.example.IHello");
		
		NettyServer server = new NettyServer(url,new MessageHandler(){

			@Override
			public Object handle(Object message) {
				if( message instanceof Request) {
					Request request = (Request)message;
					System.out.println(" server reciver request ： " + request.getId() );
					return new DefaultResponse();
				}
				return null;
			}
			
		});

        try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
