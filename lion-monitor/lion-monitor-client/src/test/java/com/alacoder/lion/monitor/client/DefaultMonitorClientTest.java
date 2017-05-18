/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-client
 * @Title: DefaultMonitorClientTest.java
 * @Package com.alacoder.lion.monitor.client
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月14日 上午11:07:14
 * @version V1.0
 */

package com.alacoder.lion.monitor.client;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.monitor.DefaultRpcMonitorMsg;
import com.alacoder.lion.remote.netty.NettyChannelHandler;
import com.alacoder.lion.remote.transport.DefaultRequest;
import com.alacoder.lion.remote.transport.Request;

/**
 * @ClassName: DefaultMonitorClientTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月14日 上午11:07:14
 *
 */

public class DefaultMonitorClientTest {

	private final static Log logger = LogFactory.getLog(DefaultMonitorClientTest.class);

	public static void main(String[] args) {
		LionURL url = new LionURL("", "", 5566, "path");
		url.addParameter(URLParamType.connectTimeout.getName(), "10000");
		url.addParameter(URLParamType.requestTimeout.getName(), "10000");
		
		final DefaultMonitorClient monitorClient = new DefaultMonitorClient(url);
		 
	    // 定时任务执行器
	    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
	    scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                	monitorClient.collectMsg( getItem());
                } catch (Throwable t) { // 防御性容错
                    logger.error("Unexpected error occur at draw stat chart, cause: " + t.getMessage(), t);
                }
            }
        }, 1, 60, TimeUnit.SECONDS);
		
	}
	
	private static DefaultRpcMonitorMsg getItem(){
		DefaultRpcMonitorMsg msg = new DefaultRpcMonitorMsg();

		msg.setApplication("app1");
		msg.setDate(new Date());
		
		int index = 5;
		
		long[] metric = new long[index];
		for(int i = 0 ;i< index; i++){
			long num1 = Math.abs(Math.round(Math.random()));
			metric[i] = num1;
		}
		msg.setMetric(metric);
		
		return msg;
	}
}
