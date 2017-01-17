/**
 * 版权声明：lion 版权所有 违者必究 2016
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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.alacoder.common.exception.LionBizException;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.utils.ReflectUtil;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;
import com.alacoder.lion.rpc.utils.LionFrameworkUtil;

/**
 * @ClassName: ProviderMessageRouter
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月23日 上午10:31:22
 *
 */

public class DefaultMessageHandler extends AbstractMessageHandler {
	
	private final static LogService logger = LogFactory.getLogService(DefaultMessageHandler.class);

	 private Map<String, Provider<?>> providers = new HashMap<String, Provider<?>>();

	    // 所有暴露出去的方法计数
	    // 比如：messageRouter 里面涉及2个Service: ServiceA 有5个public method，ServiceB
	    // 有10个public method，那么就是15
	    protected AtomicInteger methodCounter = new AtomicInteger(0);

	    public DefaultMessageHandler() {}

	    public DefaultMessageHandler(Provider<?> provider) {
	        addProvider(provider);
	    }

	    @Override
	    public Response handle(Channel channel, Request message) {
	        if (channel == null || message == null) {
	            throw new LionFrameworkException("RequestRouter handler(channel, message) params is null");
	        }

	        if (!(message instanceof Request)) {
	            throw new LionFrameworkException("RequestRouter message type not support: " + message.getClass());
	        }

	        Request request = (Request) message;

	        String serviceKey = LionFrameworkUtil.getServiceKey(request);

	        Provider<?> provider = providers.get(serviceKey);

	        if (provider == null) {
	            logger.error(this.getClass().getSimpleName() + " handler Error: provider not exist serviceKey=" + serviceKey + " "
	                    + LionFrameworkUtil.toString(request));
	            LionServiceException exception =
	                    new LionServiceException(this.getClass().getSimpleName() + " handler Error: provider not exist serviceKey="
	                            + serviceKey + " " + LionFrameworkUtil.toString(request));

	            DefaultResponse response = new DefaultResponse();
	            response.setException(exception);
	            return response;
	        }

	        return call(request, provider);
	    }

	    protected Response call(Request<?> request, Provider<?> provider) {
	        try {
	            return provider.call((Request)request);
	        } catch (Exception e) {
	        	DefaultResponse response = new DefaultResponse();
	            response.setException(new LionBizException("provider call process error", e));
	            return response;
	        }
	    }

	    public synchronized void addProvider(Provider<?> provider) {
	        String serviceKey = LionFrameworkUtil.getServiceKey(provider.getUrl());
	        if (providers.containsKey(serviceKey)) {
	            throw new LionFrameworkException("provider alread exist: " + serviceKey);
	        }

	        providers.put(serviceKey, provider);

	        // 获取该service暴露的方法数：
	        List<Method> methods = ReflectUtil.getPublicMethod(provider.getInterface());
//	        CompressRpcCodec.putMethodSign(provider, methods);// 对所有接口方法生成方法签名。适配方法签名压缩调用方式。

	        int publicMethodCount = methods.size();
	        methodCounter.addAndGet(publicMethodCount);

	        logger.info("RequestRouter addProvider: url=" + provider.getUrl() + " all_public_method_count=" + methodCounter.get());
	    }

	    public synchronized void removeProvider(Provider<?> provider) {
	        String serviceKey = LionFrameworkUtil.getServiceKey(provider.getUrl());

	        providers.remove(serviceKey);
	        List<Method> methods = ReflectUtil.getPublicMethod(provider.getInterface());
	        int publicMethodCount = methods.size();
	        methodCounter.getAndSet(methodCounter.get() - publicMethodCount);

	        logger.info("RequestRouter removeProvider: url=" + provider.getUrl() + " all_public_method_count=" + methodCounter.get());
	    }

	    public int getPublicMethodCount() {
	        return methodCounter.get();
	    }
	}

