/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-api
 * @Title: DefaultMonitorClient.java
 * @Package com.alacoder.lion.monitor.api
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月7日 下午4:21:50
 * @version V1.0
 */

package com.alacoder.lion.monitor.client;

import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.monitor.DefaultRpcMonitorMsg;
import com.alacoder.lion.monitor.MonitorMsg;
import com.alacoder.lion.monitor.MonitorMsgHandler;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.Client;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.TransportException;
import com.alacoder.lion.remote.netty.NettyClient;
import com.alacoder.lion.remote.transport.DefaultRequest;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: DefaultMonitorClient
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月7日 下午4:21:50
 *
 */

public class DefaultMonitorClient implements MonitorClient{

	private final static Log logger = LogFactory.getLog(DefaultMonitorClient.class);

	private MonitorMsgHandler handler = null;
	private Client remoteClient = null;
	
	public DefaultMonitorClient(LionURL lionURL){
		this(lionURL,null);
	}
	
	public DefaultMonitorClient(LionURL lionURL,MonitorMsgHandler handler){
		this.handler = handler;
		remoteClient = new NettyClient(lionURL,new MonitorClientMessageHandler());
		remoteClient.open();
	}

	@Override
	public boolean sendMsg(MonitorMsg msg) {
		
		return false;
	}

	@Override
	public boolean collectMsg(MonitorMsg msg) {
		Request<MonitorMsg> request = new DefaultRequest<MonitorMsg>();
		request.setRequestMsg(msg);
		try {
			remoteClient.request(request);
		} catch (TransportException t) {
			 logger.error("monitor client collect error, cause: " + t.getMessage(), t);
		}
		return false;
	}
	
	
	
	class MonitorClientMessageHandler implements MessageHandler{

		@Override
		public Response<?> handle(Channel channel, Request<?> request) {
			return null;
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
