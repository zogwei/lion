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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.alacoder.lion.registry.AbstractRegistry;
import com.alacoder.lion.registry.NotifyListener;

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.ConcurrentHashSet;

/**
 * @ClassName: ZkRegistry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月21日 下午7:14:44
 *
 */
@SpiMeta(name = "zookeeper")
public class ZkRegistry extends AbstractRegistry {
	
	private final static LogService logger = LogFactory.getLogService(ZkRegistry.class);

	private CuratorOper zkOper = null;
    private Set<LionURL> availableServices = new ConcurrentHashSet<LionURL>();
    private Map<LionURL,LionCuratorWatcher> watcherMap = new ConcurrentHashMap<LionURL,LionCuratorWatcher>();
	
    private final ReentrantLock clientLock = new ReentrantLock();
    private final ReentrantLock serverLock = new ReentrantLock();
    
	public ZkRegistry(LionURL url) {
		super(url);
		zkOper = new CuratorOper(getZkconf(url));
	}
	
	private ZkConfiguration getZkconf(LionURL url) {
		ZkConfiguration ret = null;
		String serverLists = url.getParameter("address");
		String namespace = LionConstants.ZOOKEEPER_REGISTRY_NAMESPACE.substring(1);
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
	protected void doSubscribe(final LionURL url, final NotifyListener listener) {
		try{
			clientLock.lock();
			
			LionURL urlCopy = url.createCopy();
			String path = ZkUtils.toNodeTypePath(url, ZkNodeType.AVAILABLE_SERVER);
			LionCuratorWatcher lionCuratorWatcher = new LionCuratorWatcher(listener,url,zkOper);
			
			//TODO 考虑一个path 多个 listener
			zkOper.watchChildrenChange(path, lionCuratorWatcher);
			watcherMap.put(url, lionCuratorWatcher);
			
			 List<LionURL> urls = doDiscover(urlCopy);
		     if (urls != null && urls.size() > 0) {
		    	 listener.notify(urlCopy, urls);
		     }
		}
		catch(Exception e){
			logger.error(" zk doSubscribe error,url  " + url );
			throw new LionFrameworkException(String.format("Failed to doSubscribe %s to zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
		}
		finally{
			clientLock.unlock();
		}
	}
	

	@Override
	protected void doUnsubscribe(LionURL url, NotifyListener listener) {
		try{
			clientLock.lock();
			LionCuratorWatcher lionCuratorWatcher = watcherMap.get(url);
			if(lionCuratorWatcher !=null ) {
				lionCuratorWatcher.setListener(null);
			}
		}
		catch(Exception e){
			logger.error(" zk doUnsubscribe error,url  " + url );
			throw new LionFrameworkException(String.format("Failed to doUnsubscribe %s to zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
		}
		finally{
			clientLock.unlock();
		}
		
	}

	@Override
	protected List<LionURL> doDiscover(LionURL url) {
		try {
            String parentPath = ZkUtils.toNodeTypePath(url, ZkNodeType.AVAILABLE_SERVER);
            List<String> currentChilds = new ArrayList<String>();
            if (zkOper.isExisted(parentPath)) {
                currentChilds = zkOper.getChildrenKeys(parentPath);
            }
            return nodeChildsToUrls(parentPath, currentChilds);
        } catch (Throwable e) {
            throw new LionFrameworkException(String.format("Failed to discover service %s from zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
        }
	}
	
    private List<LionURL> nodeChildsToUrls(String parentPath, List<String> currentChilds) {
        List<LionURL> urls = new ArrayList<LionURL>();
        if (currentChilds != null) {
            for (String node : currentChilds) {
                String nodePath = parentPath + LionConstants.PATH_SEPARATOR + node;
                String data = zkOper.getDirectly(nodePath);
                try {
                	LionURL url = LionURL.valueOf(data);
                    urls.add(url);
                } catch (Exception e) {
                    logger.warn(String.format("Found malformed urls from ZookeeperRegistry, path=%s", nodePath), e);
                }
            }
        }
        return urls;
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
	
	//TODO why没有版本信息，如何处理版本
	//key : /lion/'group'/'path'/command/'type[server,client,unavailableServer]'/ServerPort , value : urlfullStr
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
