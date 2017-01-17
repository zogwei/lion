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
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;
import com.alacoder.lion.rpc.remote.DefaultRpcRequest;
import com.alacoder.lion.rpc.remote.RpcRequestInfo;

/**
 * @ClassName: DefaultProviderTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午5:48:56
 *
 */

public class DefaultRefererTest {

	private final static LogService logger = LogFactory.getLogService(DefaultRefererTest.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		LionURL refererUrl = new LionURL("netty", "127.0.0.1", 4455, "com.alacoder.lion.rpc.DemoService");
		LionURL serviceUrl = new LionURL("netty", "127.0.0.1", 4455, "com.alacoder.lion.rpc.DemoService");
		serviceUrl.addParameter(URLParamType.group.getName(), "motan-demo-rpc");
		
		DefaultRpcReferer referer = new DefaultRpcReferer(DemoService.class, refererUrl , serviceUrl);
		referer.init();
		
		Request request = new DefaultRpcRequest();
		
    	RpcRequestInfo rpcRequestInfo = new RpcRequestInfo();
    	request.setRequestMsg(rpcRequestInfo);
    	rpcRequestInfo.setInterfaceName("com.alacoder.lion.rpc.DemoService");
    	rpcRequestInfo.setMethodName("hello");
    	rpcRequestInfo.setParamtersDesc("java.lang.String");
		Object arguments[] = {"Jimmy"};  
		rpcRequestInfo.setArguments(arguments);
		
		Response response = referer.call(request);
		
		logger.info("referer response value = {} ", response.getValue());

	}

}
