/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: ZkNodeStorage.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月21日 下午8:34:29
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry.zk;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.utils.LifeCycleState;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.google.common.base.Strings;

/**
 * @ClassName: ZkNodeStorage
 * @Description: 
 * @author jimmy.zhong
 * @param <ZkConfiguration>
 * @date 2016年11月21日 下午8:34:29
 *  
 *  TODO 变更 操作（persist， update 等）并发问题 
 */

public class ZkNodeStorageOper {
	
	private volatile LifeCycleState state = LifeCycleState.UNINIT;
	
    private CuratorFramework client;
    private TreeCache cache;

	public ZkNodeStorageOper(ZkConfiguration zkConfig){
		LoggerUtil.debug(" ZkNodeStorageOper init begin");
		try {
			LoggerUtil.debug("Elastic job: zookeeper registry center init, server lists is: {}.",zkConfig.getServerLists());
			Builder builder = CuratorFrameworkFactory
					.builder()
					.connectString(zkConfig.getServerLists())
					.retryPolicy(
							new ExponentialBackoffRetry(zkConfig
									.getBaseSleepTimeMilliseconds(), zkConfig
									.getMaxRetries(), zkConfig
									.getMaxSleepTimeMilliseconds()))
					.namespace(zkConfig.getNamespace());
			if (0 != zkConfig.getSessionTimeoutMilliseconds()) {
				builder.sessionTimeoutMs(zkConfig
						.getSessionTimeoutMilliseconds());
			}
			if (0 != zkConfig.getConnectionTimeoutMilliseconds()) {
				builder.connectionTimeoutMs(zkConfig
						.getConnectionTimeoutMilliseconds());
			}
			if (!Strings.isNullOrEmpty(zkConfig.getDigest())) {
				builder.authorization("digest",
						zkConfig.getDigest().getBytes(Charset.forName("UTF-8")))
						.aclProvider(new ACLProvider() {

							@Override
							public List<ACL> getDefaultAcl() {
								return ZooDefs.Ids.CREATOR_ALL_ACL;
							}

							@Override
							public List<ACL> getAclForPath(final String path) {
								return ZooDefs.Ids.CREATOR_ALL_ACL;
							}
						});
			}
			client = builder.build();
			client.start();
		    client.blockUntilConnected();
		    
	        cache = new TreeCache(client, "/");
	        cache.start();
	        
			state = LifeCycleState.INIT;
			LoggerUtil.debug(" ZkNodeStorageOper init success");
		} catch (Exception e) {
			LoggerUtil.error(" ZkNodeStorageOper error", e);
			throw new LionFrameworkException(" ZkNodeStorageOper error " , e);
		} finally {
			if(!state.isInitState()){
				close();
			}
		}
	}
	
	public void syn(boolean backgroudFlag) {
		try{
			if(backgroudFlag){
				client.sync().inBackground();
			}
			else {
				client.sync();
			}
		}
		catch(Exception ex) {
			LoggerUtil.error(" zk syn error  " );
			throw new LionFrameworkException(" zk syn error " ,ex);
		}
	}
	
	public boolean isExisted(final String key) {
		try{
			return null != client.checkExists().forPath(key);
		}
		catch(Exception ex) {
			LoggerUtil.error(" zk isExisted error,key  " + key);
			throw new LionFrameworkException(" zk isExisted error,key  " + key,ex);
		}
	}
	
	public void persist(final String key, final String value) {
		if(isExisted(key)){
			LoggerUtil.warn(" zk key is already existed, key  " + key);
			update(key,value);
		}
		else {
			try {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key, value.getBytes(Charset.forName("UTF-8")));
			} catch (Exception ex) {
				LoggerUtil.error(" zk create error,key  " + key +" ,value " + value);
				throw new LionFrameworkException(" zk create error,key  "  + key +" ,value " + value, ex);
			}
		}
	}
	
	public void persistSequential(final String key, final String value) {
		if(isExisted(key)){
			LoggerUtil.warn(" zk key is already existed, key  " + key);
			update(key,value);
		}
		else {
			try {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(key, value.getBytes(Charset.forName("UTF-8")));
			} catch (Exception ex) {
				LoggerUtil.error(" zk create error,key  " + key +" ,value " + value);
				throw new LionFrameworkException(" zk create error,key  "  + key +" ,value " + value, ex);
			}
		}
	}
    
    public void persistEphemeral(final String key, final String value) {
        try {
            if (isExisted(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(Charset.forName("UTF-8")));
        } catch (final Exception ex) {
        	LoggerUtil.error(" zk persistEphemeral error,key  " + key );
			throw new LionFrameworkException(" zk persistEphemeral error,key  "  + key, ex);
        }
    }
    
    public void persistEphemeralSequential(final String key) {
        try {
        	if (isExisted(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
        } catch (final Exception ex) {
        	LoggerUtil.error(" zk persistEphemeralSequential error,key  " + key );
			throw new LionFrameworkException(" zk persistEphemeralSequential error,key  "  + key, ex);
        }
    }
    
    public void persistEphemeralSequential(final String key, String value) {
        try {
        	if (isExisted(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key, value.getBytes(Charset.forName("UTF-8")));
        } catch (final Exception ex) {
        	LoggerUtil.error(" zk persistEphemeralSequential error,key  " + key );
			throw new LionFrameworkException(" zk persistEphemeralSequential error,key  "  + key, ex);
        }
    }
    
	
	public void update(final String key, final String value) {
		try {
			client.inTransaction().check().forPath(key).and().setData()
					.forPath(key, value.getBytes(Charset.forName("UTF-8")))
					.and().commit();
		} catch (final Exception ex) {
			LoggerUtil.error(" zk update error,key  " + key +" ,value " + value);
			throw new LionFrameworkException(" zk isExisted error,key  "  + key +" ,value " + value, ex);
		}
	}
	
	public String get(final String key) {
//		client.sync();
		if (null == cache) {
			return null;
		}
		ChildData resultIncache = cache.getCurrentData(key);
		if (null != resultIncache) {
			return null == resultIncache.getData() ? null : new String(
					resultIncache.getData(), Charset.forName("UTF-8"));
		}
		return getDirectly(key);
	}

	public String getDirectly(final String key) {
		try {
			return new String(client.getData().forPath(key),
					Charset.forName("UTF-8"));
		} catch (final Exception ex) {
			LoggerUtil.error(" zk getDirectly error,key  " + key );
			throw new LionFrameworkException(" zk getDirectly error,key  "  + key, ex);
		}
	}

	public List<String> getChildrenKeys(final String key) {
		try {
//			client.sync();
			List<String> result = client.getChildren().forPath(key);
			Collections.sort(result, new Comparator<String>() {
				@Override
				public int compare(final String o1, final String o2) {
					return o2.compareTo(o1);
				}
			});
			return result;
		} catch (final Exception ex) {
			LoggerUtil.error(" zk getChildrenKeys error,key  " + key );
			throw new LionFrameworkException(" zk getChildrenKeys error,key  "  + key, ex);
		}
	}

    public void remove(final String key) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(key);
        } catch (final Exception ex) {
        	LoggerUtil.error(" zk update error,key  " + key );
			throw new LionFrameworkException(" zk isExisted error,key  "  + key, ex);
		}
    }
	
	public void close(){
		CloseableUtils.closeQuietly(cache);
		CloseableUtils.closeQuietly(client);
	}
}
