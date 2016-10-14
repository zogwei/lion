/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractRegistryFactory.java
 * @Package com.alacoder.lion.rpc.registry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午6:46:34
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: AbstractRegistryFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午6:46:34
 *
 */

public abstract class AbstractRegistryFactory implements RegistryFactory{

	private static  ConcurrentHashMap<String, Registry> registries = new ConcurrentHashMap<String, Registry>();
	
	private static final ReentrantLock lock = new ReentrantLock();

	protected String getRegistryUri(LionURL url) {
		String registryUri = url.getUri();
		return registryUri;
	}
	
    @Override
    public Registry getRegistry(LionURL url) {
        String registryUri = getRegistryUri(url);
        try {
            lock.lock();
            Registry registry = registries.get(registryUri);
            if (registry != null) {
                return registry;
            }
            registry = createRegistry(url);
            if (registry == null) {
                throw new LionFrameworkException("Create registry false for url:" + url, LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
            }
            registries.put(registryUri, registry);
            return registry;
        } catch (Exception e) {
            throw new LionFrameworkException("Create registry false for url:" + url, e, LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
        } finally {
            lock.unlock();
        }
    }

    protected abstract Registry createRegistry(LionURL url);	
}
