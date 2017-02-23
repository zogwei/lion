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

import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;

/**
 * @ClassName: DefaultMonitorMsgHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月16日 下午5:11:03
 *
 */

public class DefaultMonitorMsgHandler implements MonitorMsgHandler {

	private final static LogService logger = LogFactory.getLogService(DefaultMonitorMsgHandler.class);
	@Override
	public void handler(MonitorMsg msg) {
		logger.warn("dong nothin, msg : " + msg.toString());
	}

}
