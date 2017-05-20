/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: CrudExample.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月1日 下午2:11:35
 * @version V1.0
 */

package com.alacoder.lion.zk.demo;

/**
 * @ClassName: CrudExample
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月1日 下午2:11:35
 *
 */

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
public class CrudExample {
	private static final String PATH = "/example/nodeCache";
	public static void main(String[] args) throws Exception {
		TestingServer server = new TestingServer();
		CuratorFramework client = null;
		NodeCache cache = null;
		String creatKey = null;
		try {
			client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
			client.start();
			cache = new NodeCache(client, PATH);
			cache.start();
			
//			CuratorListener listener = new CuratorListener() {
//				@Override
//				public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
//					System.out.println(" CuratorListenable event Received " + event);
//				}
//			};
//			Thread.sleep(1*1000);
//			client.getCuratorListenable().addListener(listener);
//			
//
//			Thread.sleep(1*1000);
//			System.out.println(">>  watchedGetChildren ");
//			creatKey = PATH+"/"+"setDataAsync"+"/sub";
//			if(null != client.checkExists().forPath(creatKey)){
//				guaranteedDelete(client,creatKey);
//			}
//			Thread.sleep(1*1000);
//			create(client,creatKey, "value".getBytes());
//			
//			watchedGetChildren(client,PATH+"/"+"setDataAsync");
//			Thread.sleep(1*1000);
////			setDataAsync(client,PATH+"/"+"setDataAsync", "value".getBytes());
////			setDataAsync(client,PATH+"/"+"setDataAsync"+"/sub", "value".getBytes());
////			setDataAsync(client,PATH+"/"+"setDataAsync"+"/sub", "value".getBytes());
//			
//			Thread.sleep(1*1000);
//			setDataAsync(client,PATH+"/"+"setDataAsync", "value".getBytes());
//			Thread.sleep(1*1000);
//			setData(client,PATH+"/"+"setDataAsync"+"/sub", "value".getBytes());
//			
//			System.out.println(">>  setDataAsyncWithCallback ");
//			BackgroundCallback callback = new BackgroundCallback(){
//				@Override
//				public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
//					System.out.println(">> callback  processResult " + event.getPath() + " type " + event.getType());
//				}
//				
//			};
//			Thread.sleep(1*1000);
//			setDataAsyncWithCallback(client,callback,PATH+"/"+"setDataAsync", "value".getBytes());
			
			
			
			System.out.println(">>  watchedGetChildren ");
			creatKey = PATH+"/"+"watchedGetChildren"+"/sub";
			if(null != client.checkExists().forPath(creatKey)){
				Thread.sleep(1*1000);
				guaranteedDelete(client,creatKey);
			}
			Thread.sleep(1*1000);
			create(client,creatKey, "value".getBytes());
			
			Watcher watcher = new Watcher(){
				@Override
				public void process(WatchedEvent event) {
					System.out.println(">>  process , event type :"+event.getType() + " path " + event.getPath() );
				}
			};
			Thread.sleep(1*1000);
			watchedGetChildren(client,PATH+"/"+"watchedGetChildren",watcher);
			
			Thread.sleep(1*1000);
			setData(client,PATH+"/"+"watchedGetChildren", "value-new".getBytes());
			Thread.sleep(1*1000);
			setData(client,PATH+"/"+"watchedGetChildren"+"/sub", "value-new".getBytes());
			create(client,PATH+"/"+"watchedGetChildren"+"/sub-2", "value-new-2".getBytes());
			if(null != client.checkExists().forPath(PATH+"/"+"watchedGetChildren"+"/sub")){
				Thread.sleep(1*1000);
				guaranteedDelete(client,PATH+"/"+"watchedGetChildren"+"/sub");
			}

			System.out.println(">>  end ");
			
			Thread.sleep(100*1000);
		}catch(Throwable e){
			e.printStackTrace();
		} finally {
			CloseableUtils.closeQuietly(cache);
			CloseableUtils.closeQuietly(client);
			CloseableUtils.closeQuietly(server);
		}
	}
	
	public static void create(CuratorFramework client, String path, byte[] payload) throws Exception {
		// this will create the given ZNode with the given data
		client.create().creatingParentsIfNeeded().forPath(path, payload);
	}
	public static void createEphemeral(CuratorFramework client, String path, byte[] payload) throws Exception {
		// this will create the given EPHEMERAL ZNode with the given data
		client.create().withMode(CreateMode.EPHEMERAL).forPath(path, payload);
	}
	public static String createEphemeralSequential(CuratorFramework client, String path, byte[] payload) throws Exception {
		// this will create the given EPHEMERAL-SEQUENTIAL ZNode with the given
		// data using Curator protection.
		return client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, payload);
	}
	public static void setData(CuratorFramework client, String path, byte[] payload) throws Exception {
		// set data for the given node
		client.setData().forPath(path, payload);
	}
	public static void setDataAsyncWithCuratorListener(CuratorFramework client, String path, byte[] payload) throws Exception {
		// this is one method of getting event/async notifications
		CuratorListener listener = new CuratorListener() {
			@Override
			public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println("eventReceived " + event);
			}
		};
		client.getCuratorListenable().addListener(listener);
		// set data for the given node asynchronously. The completion
		// notification
		// is done via the CuratorListener.
		client.setData().inBackground().forPath(path, payload);
	}
	
	public static void setDataAsync(CuratorFramework client, String path, byte[] payload) throws Exception {
		// set data for the given node asynchronously. The completion
		// notification
		// is done via the CuratorListener.
		client.setData().inBackground().forPath(path, payload);
	}
	
	public static void setDataAsyncWithCallback(CuratorFramework client, BackgroundCallback callback, String path, byte[] payload) throws Exception {
		// this is another method of getting notification of an async completion
		client.setData().inBackground(callback).forPath(path, payload);
	}
	public static void delete(CuratorFramework client, String path) throws Exception {
		// delete the given node
		client.delete().forPath(path);
	}
	public static void guaranteedDelete(CuratorFramework client, String path) throws Exception {
		// delete the given node and guarantee that it completes
		client.delete().guaranteed().forPath(path);
	}
	public static List<String> watchedGetChildren(CuratorFramework client, String path) throws Exception {
		/**
		 * Get children and set a watcher on the node. The watcher notification
		 * will come through the CuratorListener (see setDataAsync() above).
		 */
		return client.getChildren().watched().forPath(path);
	}
	public static List<String> watchedGetChildren(CuratorFramework client, String path, Watcher watcher) throws Exception {
		/**
		 * Get children and set the given watcher on the node.
		 */
		return client.getChildren().usingWatcher(watcher).forPath(path);
	}
}