/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: ZkRegistry.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月21日 下午7:14:44
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry.zk;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.ConcurrentHashSet;
import com.alacoder.lion.rpc.registry.AbstractRegistry;
import com.alacoder.lion.rpc.registry.NotifyListener;

/**
 * @ClassName: ZkRegistry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月21日 下午7:14:44
 *
 */
@SpiMeta(name = "zk")
public class ZkRegistry extends AbstractRegistry {

	private ZkNodeStorageOper zkOper = null;
    private Set<LionURL> availableServices = new ConcurrentHashSet<LionURL>();
	
    private final ReentrantLock clientLock = new ReentrantLock();
    private final ReentrantLock serverLock = new ReentrantLock();
    
	public ZkRegistry(LionURL url) {
		super(url);
		zkOper = new ZkNodeStorageOper(getZkconf(url));
	}
	
	private ZkConfiguration getZkconf(LionURL url) {
		ZkConfiguration ret = null;
		String serverLists = url.getParameter("address");
		String namespace = LionConstants.ZOOKEEPER_REGISTRY_NAMESPACE;
		int baseSleepTimeMilliseconds = url.getIntParameter(URLParamType.connectTimeout.getName(), URLParamType.connectTimeout.getIntValue());
		int maxSleepTimeMilliseconds = url.getIntParameter(URLParamType.connectTimeout.getName(), URLParamType.connectTimeout.getIntValue());
		int maxRetries = url.getIntParameter(URLParamType.retries.getName(), URLParamType.retries.getIntValue());
		ret = new ZkConfiguration(serverLists,namespace,baseSleepTimeMilliseconds,maxSleepTimeMilliseconds,maxRetries);
		
		return ret;
	}

	@Override
	protected void doRegister(LionURL url) {
		try{
			serverLock.lock();
            removeNode(url, ZkNodeType.AVAILABLE_SERVER);
            removeNode(url, ZkNodeType.UNAVAILABLE_SERVER);
            createNode(url, ZkNodeType.UNAVAILABLE_SERVER);
		}
		catch(Exception e){
			throw new LionFrameworkException(String.format("Failed to register %s to zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
		}
		finally{
			serverLock.unlock();
		}
	}

	@Override
	protected void doUnregister(LionURL url) {
		try{
			serverLock.lock();
            removeNode(url, ZkNodeType.AVAILABLE_SERVER);
            removeNode(url, ZkNodeType.UNAVAILABLE_SERVER);
		}
		catch(Exception e){
			throw new LionFrameworkException(String.format("Failed to register %s to zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
		}
		finally{
			serverLock.unlock();
		}
	}

	@Override
	protected void doSubscribe(LionURL url, NotifyListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doUnsubscribe(LionURL url, NotifyListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List<LionURL> doDiscover(LionURL url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doAvailable(LionURL url) {
		try {
			serverLock.lock();
			if (url == null) {
				availableServices.addAll(getRegisteredServiceUrls());
				for (LionURL u : getRegisteredServiceUrls()) {
					removeNode(u, ZkNodeType.AVAILABLE_SERVER);
					removeNode(u, ZkNodeType.UNAVAILABLE_SERVER);
					createNode(u, ZkNodeType.AVAILABLE_SERVER);
				}
			} else {
				availableServices.add(url);
				removeNode(url, ZkNodeType.AVAILABLE_SERVER);
				removeNode(url, ZkNodeType.UNAVAILABLE_SERVER);
				createNode(url, ZkNodeType.AVAILABLE_SERVER);
			}
		} finally {
			serverLock.unlock();
		}
	}

	@Override
	protected void doUnavailable(LionURL url) {
		try {
			serverLock.lock();
			if (url == null) {
				availableServices.removeAll(getRegisteredServiceUrls());
				for (LionURL u : getRegisteredServiceUrls()) {
					removeNode(u, ZkNodeType.AVAILABLE_SERVER);
					removeNode(u, ZkNodeType.UNAVAILABLE_SERVER);
					createNode(u, ZkNodeType.UNAVAILABLE_SERVER);
				}
			} else {
				availableServices.remove(url);
				removeNode(url, ZkNodeType.AVAILABLE_SERVER);
				removeNode(url, ZkNodeType.UNAVAILABLE_SERVER);
				createNode(url, ZkNodeType.UNAVAILABLE_SERVER);
			}
		} finally {
			serverLock.unlock();
		}
	}
	
    private void createNode(LionURL url, ZkNodeType nodeType) {
        String nodeTypePath = ZkUtils.toNodeTypePath(url, nodeType);
        if (!zkOper.isExisted(nodeTypePath)) {
        	zkOper.persist(nodeTypePath, url.toFullStr());
        }
        zkOper.persistEphemeral(ZkUtils.toNodePath(url, nodeType), url.toFullStr());
    }

    private void removeNode(LionURL url, ZkNodeType nodeType) {
        String nodePath = ZkUtils.toNodePath(url, nodeType);
        if (zkOper.isExisted(nodePath)) {
        	zkOper.remove(nodePath);
        }
    }

}
