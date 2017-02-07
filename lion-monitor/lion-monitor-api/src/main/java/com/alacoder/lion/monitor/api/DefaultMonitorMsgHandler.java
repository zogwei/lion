/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-api
 * @Title: DefaultMonitorMsgHandler.java
 * @Package com.alacoder.lion.monitor.api
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月7日 下午4:28:56
 * @version V1.0
 */

package com.alacoder.lion.monitor.api;

import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: DefaultMonitorMsgHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月7日 下午4:28:56
 *
 */

public class DefaultMonitorMsgHandler implements MonitorMsgHandler , MessageHandler{

	@Override
	public Response<?> handle(Channel channel, Request<?> request) {
		// TODO Auto-generated method stub
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
