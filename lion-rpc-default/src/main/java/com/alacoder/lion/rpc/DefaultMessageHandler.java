/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: ProviderMessageRouter.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月23日 上午10:31:22
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: ProviderMessageRouter
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月23日 上午10:31:22
 *
 */

public class DefaultMessageHandler extends AbstractMessageHandler {

	protected Response call(Request request, Provider<?> provider){
		try{
			return provider.call(request);
		}
		catch(Exception e){
			LoggerUtil.error(" call error, provider not find ,request " + request.toString());
			DefaultResponse response = new DefaultResponse();
			response.setException(e);
			return response;
		}
	}
}
