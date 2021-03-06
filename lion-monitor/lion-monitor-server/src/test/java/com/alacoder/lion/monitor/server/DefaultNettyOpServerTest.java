/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-server
 * @Title: DefaultNettyOpServerTest.java
 * @Package com.alacoder.lion.monitor.server
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月15日 下午4:52:17
 * @version V1.0
 */

package com.alacoder.lion.monitor.server;

import java.util.HashMap;
import java.util.Map;

import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.utils.HttpClientUtil;
import com.alacoder.lion.monitor.op.netty.DefaultNettyOpServer;

/**
 * @ClassName: DefaultNettyOpServerTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月15日 下午4:52:17
 *
 */

public class DefaultNettyOpServerTest {

	@SuppressWarnings("unused")
	private final static Log logger = LogFactory.getLog(DefaultNettyOpServerTest.class);
	
	static Integer   port = 8089;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		LionURL url = new LionURL("", "", 5566, "path");
		
		DefaultNettyOpServer nettyServer = new DefaultNettyOpServer(port);
		nettyServer.init();
		
        Map<String, String> maps = new HashMap<String, String>();  
        maps.put("metric", "test01");  
        maps.put("type", "123456");  
        String responseContent = HttpClientUtil.getInstance()  
                .sendHttpPost("http://localhost:"+port+"/monitor/rpc", maps);  
        System.out.println("reponse content:" + responseContent);  
	}
}
