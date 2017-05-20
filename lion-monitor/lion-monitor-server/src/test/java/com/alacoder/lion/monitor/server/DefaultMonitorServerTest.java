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

import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
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
	

	@SuppressWarnings("unused")
	private final static Log logger = LogFactory.getLog(DefaultMonitorServerTest.class);

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		LionURL url = new LionURL("", "", 5566, "path");
		DefaultMonitorServer monitorServer = new DefaultMonitorServer(url);
	}

}
