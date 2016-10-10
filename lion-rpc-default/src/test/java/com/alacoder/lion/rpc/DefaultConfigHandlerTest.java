/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-default
 * @Title: DefaultConfigHandler.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 上午11:56:33
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.util.ArrayList;
import java.util.List;

import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.url.URL;

/**
 * @ClassName: DefaultConfigHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 上午11:56:33
 *
 */

public class DefaultConfigHandlerTest {

	/**
	 * main(这里用一句话描述这个方法的作用)
	 *
	 * @Title: main
	 * @Description: 
	 * @param @param args    设定文件
	 * @return void    返回类型
	 * @throws
	 */

	public static void main(String[] args) {
		
		String serviceKey = "motan://10.12.104.6:8001/com.weibo.motan.demo.service.MotanDemoService?"
				+ "protocol=motan&"
				+ "export=demoMotan:8001&"
				+ "application=myMotanDemo&"
				+ "module=motan-demo-rpc&"
				+ "refreshTimestamp=1474862047591&"
				+ "maxContentLength=1048576&"
				+ "id=com.weibo.api.motan.config.springsupport.ServiceConfigBean&"
				+ "maxWorkerThread=800&"
				+ "maxServerConnection=80000&"
				+ "accessLog=false&"
				+ "isDefault=true&"
				+ "minWorkerThread=20&"
				+ "group=motan-demo-rpc&"
				+ "nodeType=service&"
				+ "shareChannel=true&";
		
		String embed = "lion://10.12.104.6:4455/com.alacoder.lion.rpc.DemoService?"
				+ "protocol=lion&"
				+ "export=demoMotan:8001&"
				+ "application=myMotanDemo&"
				+ "module=motan-demo-rpc&"
				+ "refreshTimestamp=1474862047591&"
				+ "maxContentLength=1048576&"
				+ "id=com.weibo.api.motan.config.springsupport.ServiceConfigBean&"
				+ "maxWorkerThread=800&"
				+ "maxServerConnection=80000&"
				+ "accessLog=false&"
				+ "isDefault=true&"
				+ "minWorkerThread=20&"
				+ "group=motan-demo-rpc&"
				+ "nodeType=service&"
				+ "address=10.12.104.6:4455&"
				+ "shareChannel=true&";

		URL url = new URL(LionConstants.REGISTRY_PROTOCOL_DIRECT, "10.12.104.6", 4455, "com.alacoder.lion.rpc.DemoService");
		url.addParameter("embed", embed);
		url.addParameter("protocol", LionConstants.REGISTRY_PROTOCOL_DIRECT);
		url.addParameter("export", "demoMotan:8001");
		url.addParameter("application", "myMotanDemo");
		url.addParameter("module", "motan-demo-rpc");
		url.addParameter("refreshTimestamp", "1474862047591");
		url.addParameter("maxContentLength", "1048576");
		url.addParameter("id", "com.weibo.api.motan.config.springsupport.ServiceConfigBean");
		url.addParameter("maxWorkerThread", "800");
		url.addParameter("maxServerConnection", "80000");
		url.addParameter("accessLog", "false");
		url.addParameter("minWorkerThread", "20");
		url.addParameter("group", "motan-demo-rpc");
		url.addParameter("nodeType", "service");
		url.addParameter("shareChannel", "true");
		url.addParameter("address", "10.12.104.6:4455");
		
		List<URL> protocolURL = new ArrayList<URL>();
		protocolURL.add(url);
		
		DefaultConfigHandler configHandler = new DefaultConfigHandler();
		DemoService service = new DemoServiceImple();
		
		configHandler.export(DemoService.class, service, protocolURL);
		
		
	}

}
