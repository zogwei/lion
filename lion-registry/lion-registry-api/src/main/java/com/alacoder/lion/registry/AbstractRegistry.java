/**
 * 版权声明：lion 版权所有 违者必究 2016
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

package com.alacoder.lion.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.ConcurrentHashSet;

/**
 * @ClassName: AbstractRegistry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午10:35:02
 *
 */

public abstract class AbstractRegistry implements Registry {
	
	private final static Log logger = LogFactory.getLog(AbstractRegistry.class);
	
	 private ConcurrentHashMap<LionURL, Map<String, List<LionURL>>> subscribedCategoryResponses = new ConcurrentHashMap<LionURL, Map<String, List<LionURL>>>();
	 private LionURL registryUrl;
	 protected Set<LionURL> registeredServiceUrls = new ConcurrentHashSet<LionURL>();
	 protected String registryClassName = this.getClass().getSimpleName();
	 
	 public AbstractRegistry(LionURL url) {
		 this.registryUrl = url.createCopy();
		 //TODO register a heartbeat switcher to perceive service state change and change available state
	 }

	 @Override
	 public void register(LionURL url) {
		if (url == null) {
			logger.warn("[{}] register with malformed param, url is null", registryClassName);
			return;
		}
		logger.info("[{}] Url ({}) will register to Registry [{}]", registryClassName, url, registryUrl.getIdentity());
		doRegister(removeUnnecessaryParmas(url.createCopy()));
		registeredServiceUrls.add(url);
	 }

	/**
	 * 移除不必提交到注册中心的参数。这些参数不需要被client端感知。
	 *
	 * @param url
	 */
	private LionURL removeUnnecessaryParmas(LionURL url) {
		// codec参数不能提交到注册中心，如果client端没有对应的codec会导致client端不能正常请求。
		url.getParameters().remove(URLParamType.codec.getName());
		return url;
	}
	
	@Override
	public void unregister(LionURL url) {
		if( url == null) {
			 logger.warn("[{}] unregister with malformed param, url is null", registryClassName);
	            return;
		}
		logger.info("[{}] Url ({}) will unregister to Registry [{}]", registryClassName, url, registryUrl.getIdentity());
		doUnregister(removeUnnecessaryParmas(url.createCopy()));
		registeredServiceUrls.remove(url);
		 
	}
	
    @Override
    public void subscribe(LionURL url, NotifyListener listener) {
        if (url == null || listener == null) {
            logger.warn("[{}] subscribe with malformed param, url:{}, listener:{}", registryClassName, url, listener);
            return;
        }
        logger.info("[{}] Listener ({}) will subscribe to url ({}) in Registry [{}]", registryClassName, listener, url,
                registryUrl.getIdentity());
        doSubscribe(url.createCopy(), listener);
    }
    
    @Override
    public void unsubscribe(LionURL url, NotifyListener listener) {
        if (url == null || listener == null) {
            logger.warn("[{}] unsubscribe with malformed param, url:{}, listener:{}", registryClassName, url, listener);
            return;
        }
        logger.info("[{}] Listener ({}) will unsubscribe from url ({}) in Registry [{}]", registryClassName, listener, url,
                registryUrl.getIdentity());
        doUnsubscribe(url.createCopy(), listener);
    }
    
//    @Override
//    public void available(LionURL url) {
//        logger.info("[{}] Url ({}) will set to available to Registry [{}]", registryClassName, url, registryUrl.getIdentity());
//        if (url != null) {
//            doAvailable(removeUnnecessaryParmas(url.createCopy()));
//        } else {
//            doAvailable(null);
//        }
//    }
//
//    @Override
//    public void unavailable(LionURL url) {
//        logger.info("[{}] Url ({}) will set to unavailable to Registry [{}]", registryClassName, url, registryUrl.getIdentity());
//        if (url != null) {
//            doUnavailable(removeUnnecessaryParmas(url.createCopy()));
//        } else {
//            doUnavailable(null);
//        }
//    }
    
	@Override
	public Collection<LionURL> getRegisteredServiceUrls() {
		return registeredServiceUrls;
	}
	

    @SuppressWarnings("unchecked")
    @Override
    public List<LionURL> discover(LionURL url) {
        if (url == null) {
            logger.warn("[{}] discover with malformed param, refUrl is null", registryClassName);
            return Collections.EMPTY_LIST;
        }
        url = url.createCopy();
        List<LionURL> results = new ArrayList<LionURL>();

        Map<String, List<LionURL>> categoryUrls = subscribedCategoryResponses.get(url);
        if (categoryUrls != null && categoryUrls.size() > 0) {
            for (List<LionURL> urls : categoryUrls.values()) {
                for (LionURL tempUrl : urls) {
                    results.add(tempUrl.createCopy());
                }
            }
        } else {
            List<LionURL> urlsDiscovered = doDiscover(url);
            if (urlsDiscovered != null) {
                for (LionURL u : urlsDiscovered) {
                    results.add(u.createCopy());
                }
            }
        }
        return results;
    }

    
	@Override
	public LionURL getUrl() {
		return this.registryUrl;
	}
	
    protected abstract void doRegister(LionURL url);

    protected abstract void doUnregister(LionURL url);

    protected abstract void doSubscribe(LionURL url, NotifyListener listener);

    protected abstract void doUnsubscribe(LionURL url, NotifyListener listener);

    protected abstract List<LionURL> doDiscover(LionURL url);

//    protected abstract void doAvailable(LionURL url);
//
//    protected abstract void doUnavailable(LionURL url);
}
