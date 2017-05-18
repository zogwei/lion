     
 /* 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-server
 * @Title: DisputorMsgHandler.java
 * @Package com.alacoder.lion.monitor.server
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月16日 下午4:50:17
 * @version V1.0
 */

package com.alacoder.lion.monitor.server;

import com.aben.cup.log.logging.Log;
import com.aben.cup.log.logging.internal.Log4JLoggerFactory;
import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.monitor.MonitorMsg;
import com.alacoder.lion.monitor.MonitorMsgHandler;
import com.alacoder.lion.monitor.op.netty.DefaultNettyOpServer;
import com.alacoder.lion.remote.transport.Request;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * @ClassName: DisputorMsgHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月16日 下午4:50:17
 *
 */

public class DisputorMsgHandler implements EventHandler<Request<MonitorMsg>>,WorkHandler<Request<MonitorMsg>> {
	
	MonitorMsgHandler handler = null;

	private final static Log logger = LogFactory.getLog(DisputorMsgHandler.class);
	
	public DisputorMsgHandler(MonitorMsgHandler handler){
		this.handler =  handler;
	}

	@Override
	public void onEvent(Request<MonitorMsg> event) throws Exception {
		// 这里做具体的消费逻辑
		logger.info(" DisputorMsgHandler  " + event.toString());
		handler.handler(event.getRequestMsg());

	}

	@Override
	public void onEvent(Request<MonitorMsg> event, long sequence, boolean endOfBatch)
			throws Exception {
		this.onEvent(event);
	}

}
