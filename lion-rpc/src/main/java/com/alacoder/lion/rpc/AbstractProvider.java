/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractProvider.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午4:39:04
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.utils.ReflectUtil;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: AbstractProvider
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午4:39:04
 *
 */

public abstract class AbstractProvider<T> implements Provider<T> {
	protected Class<T> clz;
	protected LionURL url;
	protected boolean alive = false;
	protected boolean close = false;
	
	protected Map<String, Method> methodMap = new HashMap<String, Method>();
	
	public AbstractProvider(LionURL url,Class<T> clz){
		this.clz = clz;
		this.url = url;
		
		initMethodMap(clz);
	}
	
	private void initMethodMap(Class<T> clz){
		Method[] methods = clz.getMethods();
		for(Method method : methods){
			String methodDesc = ReflectUtil.getMethodDesc(method);
			methodMap.put(methodDesc, method);
		}
	}

	@Override
	public Response call(Request request) {
		Response response = invoke(request);
		return response;
	}
	
	protected abstract Response invoke(Request reqeust);
	
    protected Method lookup(Request request) {
        String methodDesc = ReflectUtil.getMethodDesc(request.getMethodName(), request.getParamtersDesc());

        return methodMap.get(methodDesc);
    }

	@Override
	public void init() {
		 alive = true;
	}

	@Override
	public void destroy() {
		 alive = false;
	     close = true;
	}

	@Override
	public boolean isAvailable() {
		return alive;
	}

	@Override
	public String desc() {
		 if (url != null) {
	            return url.toString();
	        }

	        return null;
	}

	@Override
	public LionURL getUrl() {
		return url;
	}

	@Override
	public Class<T> getInterface() {
		return clz;
	}

}
