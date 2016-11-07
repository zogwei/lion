/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: DefaultProviderTest.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午5:48:56
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.transport.DefaultRequest;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: DefaultProviderTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午5:48:56
 *
 */

public class DefaultRefererTest {


	public static void main(String[] args) {
		LionURL refererUrl = new LionURL("netty", "10.12.104.6", 4455, "com.alacoder.lion.rpc.DemoService");
		LionURL serviceUrl = new LionURL("netty", "10.12.104.6", 4455, "com.alacoder.lion.rpc.DemoService");
		serviceUrl.addParameter(URLParamType.group.getName(), "motan-demo-rpc");
		
		DefaultRpcReferer referer = new DefaultRpcReferer(DemoService.class, refererUrl , serviceUrl);
		referer.init();
		
		Request request = new DefaultRequest();
		request.setInterfaceName("com.alacoder.lion.rpc.DemoService");
		request.setMethodName("hello");
		request.setParamtersDesc("java.lang.String");
		Object arguments[] = {"Jimmy"};  
		request.setArguments(arguments);
		
		Response response = referer.call(request);
		
		LoggerUtil.info("referer response value = {} ", response.getValue());

	}

}
