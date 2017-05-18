/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-core
 * @Title: DefaultMonitorMsgHandler.java
 * @Package com.alacoder.lion.monitor
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月16日 下午5:11:03
 * @version V1.0
 */

package com.alacoder.lion.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;

/**
 * @ClassName: DefaultMonitorMsgHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月16日 下午5:11:03
 *
 */

public class DefaultMonitorMsgHandler implements MonitorMsgHandler {

	private final static Log logger = LogFactory.getLog(DefaultMonitorMsgHandler.class);
	
	//内存存储
	private final static Map<String,Map> monitorData = new ConcurrentHashMap<String,Map>();
	
	@Override
	public void handler(MonitorMsg msg) {
		logger.info("dong nothin, msg : " + msg.toString());
		String name = msg.getMetricType();

	}

}
