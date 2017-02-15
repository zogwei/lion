/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-api
 * @Title: DefaultMonitorServer.java
 * @Package com.alacoder.lion.monitor.api
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月7日 下午4:22:21
 * @version V1.0
 */

package com.alacoder.lion.monitor.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.monitor.DefaultRpcMonitorMsg;
import com.alacoder.lion.monitor.MonitorMsgHandler;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.Server;
import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.netty.NettyServer;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: DefaultMonitorServer
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月7日 下午4:22:21
 *
 */

public class DefaultMonitorServer implements MonitorServer{

	private final static LogService logger = LogFactory.getLogService(DefaultMonitorServer.class);

	private final static Integer MAX_QUEUE= 10000;
	
	private MonitorMsgHandler handler = null;
	private Server remoteServer = null;
	
	//TODO 异常处理相关 考虑改成disputor实现
    private final BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>(MAX_QUEUE);
    
			
	public DefaultMonitorServer(LionURL lionURL){
		this(lionURL,null);
	}
	
	public DefaultMonitorServer(LionURL lionURL,MonitorMsgHandler handler){
		this.handler = handler;
		remoteServer = new NettyServer(lionURL, new MonitorMessageHandler());
		remoteServer.open();
	}
	
	class MonitorMessageHandler implements MessageHandler{

		@Override
		public Response<?> handle(Channel channel, Request<?> request) {
			Object msg = request.getRequestMsg();
			if(msg instanceof DefaultRpcMonitorMsg) {
				logger.info(msg.toString());
				queue.add(request);
			}
			else {
				logger.error("no know type , msg : " + msg.toString());
			}
			
			DefaultResponse<String> ret = new DefaultResponse<String>();
			ret.setRequestId(request.getId());
			
			return ret;
		}

		@Override
		public Object handle(Channel channel, Response<?> response) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object handle(Channel channel, TransportData response) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	
}
