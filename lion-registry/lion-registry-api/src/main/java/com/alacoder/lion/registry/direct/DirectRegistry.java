/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: DirectRegistry.java
 * @Package com.alacoder.lion.rpc.registry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午10:45:15
 * @version V1.0
 */

package com.alacoder.lion.registry.direct;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.alacoder.lion.registry.AbstractRegistry;
import com.alacoder.lion.registry.NotifyListener;

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: DirectRegistry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午10:45:15
 *
 */
@SpiMeta(name = "direct")
public class DirectRegistry extends AbstractRegistry {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ConcurrentHashMap<LionURL,Object> subscribeUrl = new ConcurrentHashMap();
	private List<LionURL> directUrls = new ArrayList<LionURL>();

	public DirectRegistry(LionURL url) {
		super(url);
		String address = url.getParameter("address");
		if(address!=null&&address.contains(",")) {
			try {
				String[] directUrlArray = address.split(",");
				for(String directUrl : directUrlArray) {
					parseDirectUrl(directUrl);
				}
			} catch (Exception e) {
                throw new LionFrameworkException(
                        String.format("parse direct url error, invalid direct registry address %s, address should be ip1:port1,ip2:port2 ..."));
            }
        } else {
            registerDirectUrl(url.getHost(), url.getPort());
        }
	}

	private void parseDirectUrl(String directUrl) {
	     String[] ipAndPort = directUrl.split(":");
	     String ip = ipAndPort[0];
	     Integer port = Integer.parseInt(ipAndPort[1]);
	     if(port < 0 || port > 65535) {
	    	 throw new RuntimeException();
	     }
	     registerDirectUrl(ip,port);
	}
	
	private void registerDirectUrl(String ip, Integer port) {
		 LionURL url = new LionURL(LionConstants.REGISTRY_PROTOCOL_DIRECT,ip,port,"");
	     directUrls.add(url);
	}

	@Override
	protected void doSubscribe(LionURL url, NotifyListener listener) {
		subscribeUrl.putIfAbsent(url, 1);
        listener.notify(this.getUrl(), doDiscover(url));
	}

    @Override
    protected void doUnsubscribe(LionURL url, NotifyListener listener) {
    	subscribeUrl.remove(url);
        listener.notify(this.getUrl(), doDiscover(url));
    }

	@Override
	protected List<LionURL> doDiscover(LionURL url) {
		return createSubscribeUrl(url);
	}
	
    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private List<LionURL> createSubscribeUrl(LionURL subscribeUrl) {
        LionURL url = this.getUrl();
        List result = new ArrayList(registeredServiceUrls.size());
        for (LionURL directUrl : registeredServiceUrls) {
            LionURL tmp = subscribeUrl.createCopy();
            tmp.setHost(directUrl.getHost());
            tmp.setPort(directUrl.getPort());
            result.add(tmp);
        }
        return result;
    }

//	@Override
//	protected void doAvailable(LionURL url) {
//		
//	}
//
//	@Override
//	protected void doUnavailable(LionURL url) {
//		
//	}
	
	@Override
	protected void doRegister(LionURL url) {
	}

	@Override
	protected void doUnregister(LionURL url) {
	}
}
