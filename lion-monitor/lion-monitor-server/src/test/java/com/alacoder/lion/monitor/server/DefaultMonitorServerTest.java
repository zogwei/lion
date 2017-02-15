/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-server
 * @Title: DefaultMonitorServerTest.java
 * @Package com.alacoder.lion.monitor.server
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月14日 上午10:59:17
 * @version V1.0
 */

package com.alacoder.lion.monitor.server;

import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: DefaultMonitorServerTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月14日 上午10:59:17
 *
 */

public class DefaultMonitorServerTest {

	/**
	 * test 
	 *
	 * @Title: main
	 * @Description: 
	 * @param @param args    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	

	private final static LogService logger = LogFactory.getLogService(DefaultMonitorServerTest.class);

	public static void main(String[] args) {
		LionURL url = new LionURL("", "", 5566, "path");
		DefaultMonitorServer monitorServer = new DefaultMonitorServer(url);
	}

}
