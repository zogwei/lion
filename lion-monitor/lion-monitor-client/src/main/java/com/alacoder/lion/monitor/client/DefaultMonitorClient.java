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

import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.monitor.MonitorMsg;
import com.alacoder.lion.monitor.MonitorMsgHandler;
import com.alacoder.lion.remote.Client;

/**
 * @ClassName: DefaultMonitorClient
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月7日 下午4:21:50
 *
 */

public class DefaultMonitorClient implements MonitorClient{
	
	
	private MonitorMsgHandler handler = null;
	private Client remoteClient = null;
	
	public DefaultMonitorClient(LionURL lionURL){
		this(lionURL,null);
	}
	
	public DefaultMonitorClient(LionURL lionURL,MonitorMsgHandler handler){
		
	}

	@Override
	public boolean sendMsg(MonitorMsg msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean collectMsg(MonitorMsg msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
