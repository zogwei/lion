/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractMessageHandler.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 下午4:32:41
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.util.concurrent.ConcurrentHashMap;

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;
import com.alacoder.lion.rpc.utils.LionFrameworkUtil;

/**
 * @ClassName: AbstractMessageHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 下午4:32:41
 *
 */

public abstract class AbstractMessageHandler implements MessageHandler {
	protected ConcurrentHashMap<String,Provider<?>> providers = new ConcurrentHashMap<String,Provider<?>>();
	
	@Override
	public Object handle(Channel channel, Object message) {
		
		Request request = (Request)message;
		String serviceKey = LionFrameworkUtil.getServiceKey(request);
		Provider provider = providers.get(serviceKey);
		if(provider == null) {
			LoggerUtil.error(" handler error, provider not find ,serviceKey " + serviceKey);
			LionFrameworkException exception =  new LionFrameworkException(" handler error, provider not find ,serviceKey " + serviceKey);
			DefaultResponse response = new DefaultResponse();
			response.setException(exception);
			return response;
		}
		
		return call(request, provider);
	}
	
	protected abstract Response call(Request request, Provider<?> provider);
	
	public void addProvider(Provider<?> provider){
		String serviceKey = LionFrameworkUtil.getServiceKey(provider.getUrl());
		if(providers.contains(serviceKey)) {
			throw new LionFrameworkException("provider already exixt: " + serviceKey);
		}
		
		providers.put(serviceKey, provider);
		LoggerUtil.debug(" add service key " + serviceKey);
	}
	
	public void removeProvider(Provider<?> provider) {
		String serviceKey = LionFrameworkUtil.getServiceKey(provider.getUrl());
		providers.remove(serviceKey);
		LoggerUtil.debug(" remove service key " + serviceKey);
	}

}
