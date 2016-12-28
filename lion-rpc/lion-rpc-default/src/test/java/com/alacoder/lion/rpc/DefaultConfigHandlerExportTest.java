/**
 * 版权声明：lion 版权所有 违者必究 2016
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
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: DefaultConfigHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 上午11:56:33
 *
 */

public class DefaultConfigHandlerExportTest {

	public static void main(String[] args) {
		
		String embed = "lion://127.0.0.1:4455/com.alacoder.lion.rpc.DemoService?"
//				+ "protocol=lion&"
//				+ "export=demoMotan:8001&"
//				+ "application=myMotanDemo&"
//				+ "module=lion-demo-rpc&"
//				+ "refreshTimestamp=1474862047591&"
//				+ "maxContentLength=1048576&"
//				+ "id=com.weibo.api.lion.config.springsupport.ServiceConfigBean&"
//				+ "maxWorkerThread=800&"
//				+ "maxServerConnection=80000&"
//				+ "accessLog=false&"
//				+ "isDefault=true&"
//				+ "minWorkerThread=20&"
//				+ "group=lion-demo-rpc&"
//				+ "nodeType=service&"
//				+ "address=127.0.0.1:4455&"
//				+ "shareChannel=true&"
					;

		LionURL url = new LionURL(LionConstants.REGISTRY_PROTOCOL_DIRECT, "127.0.0.1", 4455, "com.alacoder.lion.rpc.DemoService");
		url.addParameter("embed", embed);
		url.addParameter("protocol", LionConstants.REGISTRY_PROTOCOL_DIRECT);
//		url.addParameter("export", "demoMotan:8001");
//		url.addParameter("application", "myMotanDemo");
//		url.addParameter("module", "lion-demo-rpc");
//		url.addParameter("refreshTimestamp", "1474862047591");
//		url.addParameter("maxContentLength", "1048576");
//		url.addParameter("id", "com.weibo.api.lion.config.springsupport.ServiceConfigBean");
//		url.addParameter("maxWorkerThread", "800");
//		url.addParameter("maxServerConnection", "80000");
//		url.addParameter("accessLog", "false");
//		url.addParameter("minWorkerThread", "20");
//		url.addParameter("group", "lion-demo-rpc");
//		url.addParameter("nodeType", "service");
//		url.addParameter("shareChannel", "true");
//		url.addParameter("address", "127.0.0.1:4455")
		;
		
		List<LionURL> protocolURL = new ArrayList<LionURL>();
		protocolURL.add(url);
		
		DefaultConfigHandler configHandler = new DefaultConfigHandler();
		DemoService service = new DemoServiceImple();
		
		configHandler.export(DemoService.class, service, protocolURL);
		
		
	}

}
