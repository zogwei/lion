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

import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.url.LionURL;
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

public class DefaultProviderTest {
	
	private final static LogService logger = LogFactory.getLogService(DefaultProviderTest.class);


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		LionURL url = new LionURL("netty", "127.0.0.1", 4455, "com.alacoder.lion.rpc.DemoService");
		DefaultProvider provider = new DefaultProvider(new DemoServiceImple(), url , DemoService.class);
		Request request = new DefaultRequest();
		request.setInterfaceName("com.alacoder.lion.rpc.DemoService");
		request.setMethodName("hello");
		request.setParamtersDesc("java.lang.String");
		Object arguments[] = {"Jimmy"};  
		request.setArguments(arguments);
		
		Response response = provider.call(request);
		
		logger.info("provider response value = {} ", response.getValue());

	}

}
