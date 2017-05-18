/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-server
 * @Title: DefaultAPIServer.java
 * @Package com.alacoder.lion.monitor.op
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月8日 上午10:56:16
 * @version V1.0
 */

package com.alacoder.lion.monitor.op.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.monitor.op.OpServer;

/**
 * @ClassName: DefaultAPIServer netty http
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月8日 上午10:56:16
 *
 */

public class DefaultNettyOpServer implements OpServer {
	
	@SuppressWarnings("unused")
	private final static Log logger = LogFactory.getLog(DefaultNettyOpServer.class);
	
	int port ;
	ServerBootstrap server = null;
	Channel ch = null;
	
	public DefaultNettyOpServer(int port){
		this.port = port;
	}

	public void init() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
		    server = new ServerBootstrap();
		    server.option(ChannelOption.SO_BACKLOG, 1024);
		    server.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new HttpNettyServerInitializer());

			ch = server.bind(port).sync().channel();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
