/**
 * 版权声明：bee 版权所有 违者必究 2016
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

package com.alacoder.lion.rpc.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.URL;

/**
 * @ClassName: DirectRegistry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午10:45:15
 *
 */
@SpiMeta(name = "direct")
public class DirectRegistry extends AbstractRegistry {
	private ConcurrentHashMap<URL,Object> subscribeUrl = new ConcurrentHashMap();
	private List<URL> directUrls = new ArrayList<URL>();

	public DirectRegistry(URL url) {
		super(url);
		String address = url.getParameter("address");
		if(address.contains(",")) {
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
		 URL url = new URL(LionConstants.REGISTRY_PROTOCOL_DIRECT,ip,port,"");
	     directUrls.add(url);
	}
	
	@Override
	public URL getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void available(URL url) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unavailable(URL url) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<URL> getRegisteredServiceUrls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void subscribe(URL url, NotifyListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsubscribe(URL url, NotifyListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<URL> discover(URL url) {
		return createSubscribeUrl(url);
	}
	
    private List<URL> createSubscribeUrl(URL subscribeUrl) {
        URL url = this.getUrl();
        List result = new ArrayList(directUrls.size());
        for (URL directUrl : directUrls) {
            URL tmp = subscribeUrl.createCopy();
            tmp.setHost(directUrl.getHost());
            tmp.setPort(directUrl.getPort());
            result.add(tmp);
        }
        return result;
    }

	@Override
	protected void doRegister(URL url) {

	}

	@Override
	protected void doUnregister(URL url) {
		
	}

	@Override
	protected void doSubscribe(URL url, NotifyListener listener) {
		subscribeUrl.putIfAbsent(url, 1);
        listener.notify(this.getUrl(), doDiscover(url));
	}

	@Override
	protected void doUnsubscribe(URL url, NotifyListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List<URL> doDiscover(URL url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doAvailable(URL url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doUnavailable(URL url) {
		// TODO Auto-generated method stub
		
	}

}
