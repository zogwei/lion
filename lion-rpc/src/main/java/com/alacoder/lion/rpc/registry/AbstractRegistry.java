/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractRegistry.java
 * @Package com.alacoder.lion.rpc.registry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午10:35:02
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alacoder.lion.common.url.URL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.ConcurrentHashSet;
import com.alacoder.lion.common.utils.LoggerUtil;

/**
 * @ClassName: AbstractRegistry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午10:35:02
 *
 */

public abstract class AbstractRegistry implements Registry {
	
	 private ConcurrentHashMap<URL, Map<String, List<URL>>> subscribedCategoryResponses =
	            new ConcurrentHashMap<URL, Map<String, List<URL>>>();
	 private URL registryUrl;
	 private Set<URL> registeredServiceUrls = new ConcurrentHashSet<URL>();
	 protected String registryClassName = this.getClass().getSimpleName();
	 
	 public AbstractRegistry(URL url) {
		 this.registryUrl = url.createCopy();
		 //TODO register a heartbeat switcher to perceive service state change and change available state
	 }

	 @Override
	 public void register(URL url) {
		if (url == null) {
			LoggerUtil.warn("[{}] register with malformed param, url is null", registryClassName);
			return;
		}
		LoggerUtil.info("[{}] Url ({}) will register to Registry [{}]", registryClassName, url, registryUrl.getIdentity());
		doRegister(removeUnnecessaryParmas(url.createCopy()));
		registeredServiceUrls.add(url);
	 }

	/**
	 * 移除不必提交到注册中心的参数。这些参数不需要被client端感知。
	 *
	 * @param url
	 */
	private URL removeUnnecessaryParmas(URL url) {
		// codec参数不能提交到注册中心，如果client端没有对应的codec会导致client端不能正常请求。
		url.getParameters().remove(URLParamType.codec.getName());
		return url;
	}
	
	@Override
	public void unregister(URL url) {
		if( url == null) {
			 LoggerUtil.warn("[{}] unregister with malformed param, url is null", registryClassName);
	            return;
		}
		LoggerUtil.info("[{}] Url ({}) will unregister to Registry [{}]", registryClassName, url, registryUrl.getIdentity());
		doUnregister(removeUnnecessaryParmas(url.createCopy()));
		registeredServiceUrls.remove(url);
		 
	}
	
    @Override
    public void subscribe(URL url, NotifyListener listener) {
        if (url == null || listener == null) {
            LoggerUtil.warn("[{}] subscribe with malformed param, url:{}, listener:{}", registryClassName, url, listener);
            return;
        }
        LoggerUtil.info("[{}] Listener ({}) will subscribe to url ({}) in Registry [{}]", registryClassName, listener, url,
                registryUrl.getIdentity());
        doSubscribe(url.createCopy(), listener);
    }
    
    @Override
    public void unsubscribe(URL url, NotifyListener listener) {
        if (url == null || listener == null) {
            LoggerUtil.warn("[{}] unsubscribe with malformed param, url:{}, listener:{}", registryClassName, url, listener);
            return;
        }
        LoggerUtil.info("[{}] Listener ({}) will unsubscribe from url ({}) in Registry [{}]", registryClassName, listener, url,
                registryUrl.getIdentity());
        doUnsubscribe(url.createCopy(), listener);
    }
    
    @Override
    public void available(URL url) {
        LoggerUtil.info("[{}] Url ({}) will set to available to Registry [{}]", registryClassName, url, registryUrl.getIdentity());
        if (url != null) {
            doAvailable(removeUnnecessaryParmas(url.createCopy()));
        } else {
            doAvailable(null);
        }
    }

    @Override
    public void unavailable(URL url) {
        LoggerUtil.info("[{}] Url ({}) will set to unavailable to Registry [{}]", registryClassName, url, registryUrl.getIdentity());
        if (url != null) {
            doUnavailable(removeUnnecessaryParmas(url.createCopy()));
        } else {
            doUnavailable(null);
        }
    }


    protected abstract void doRegister(URL url);

    protected abstract void doUnregister(URL url);

    protected abstract void doSubscribe(URL url, NotifyListener listener);

    protected abstract void doUnsubscribe(URL url, NotifyListener listener);

    protected abstract List<URL> doDiscover(URL url);

    protected abstract void doAvailable(URL url);

    protected abstract void doUnavailable(URL url);
}
