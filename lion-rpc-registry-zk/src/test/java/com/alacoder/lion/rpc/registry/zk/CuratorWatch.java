/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: CarutorDemo.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月1日 下午4:25:53
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry.zk;

/**
 * @ClassName: CarutorDemo
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月1日 下午4:25:53
 *
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Curator事件监听
 * @author  huey
 * @version 1.0 
 * @created 2015-3-2
 */
public class CuratorWatch {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(5000)
            .connectionTimeoutMs(3000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
        
        
        if(null != client.checkExists().forPath("/zk-huey")){
        	client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/zk-huey");
		}
        
        client.create()
            .creatingParentsIfNeeded()
            .forPath("/zk-huey/cnode", "hello".getBytes());
        
        /**
         * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
         */
        ExecutorService pool = Executors.newFixedThreadPool(2);
        
        /**
         * 监听数据节点的变化情况
         */
        final NodeCache nodeCache = new NodeCache(client, "/zk-huey/cnode", false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(
            new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    System.out.println("Node data is changed, new data: " + 
                        new String(nodeCache.getCurrentData().getData()));
                }
            }, 
            pool
        );
        
        client.setData().forPath("/zk-huey", "/zk-huey/-value".getBytes());
        client.setData().forPath("/zk-huey/cnode", "/zk-huey/cnode/newvalue".getBytes());
        
        
        /**
         * 监听子节点的变化情况
         */
        final PathChildrenCache childrenCache = new PathChildrenCache(client, "/zk-huey/cnodechild", true);
        childrenCache.start(StartMode.NORMAL);
        childrenCache.getListenable().addListener(
            new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                        throws Exception {
                        switch (event.getType()) {
                        case CHILD_ADDED:
                            System.out.println("CHILD_ADDED: " + event.getData().getPath());
                            break;
                        case CHILD_REMOVED:
                            System.out.println("CHILD_REMOVED: " + event.getData().getPath());
                            break;
                        case CHILD_UPDATED:
                            System.out.println("CHILD_UPDATED: " + event.getData().getPath());
                            break;
                        default:
                        	System.out.println("default: " + event.getData().getPath());
                            break;
                    }
                }
            },
            pool
        );
        

        client.create().forPath("/zk-huey/cnodechild", "/zk-huey/cnode-1".getBytes());
        client.create().forPath("/zk-huey/cnodechild/sub", "/zk-huey/cnode/sub".getBytes());
        
        client.setData().forPath("/zk-huey/cnodechild/sub", "/zk-huey/cnode/sub-new".getBytes());
        client.create().forPath("/zk-huey/cnodechild/sub/sub", "/zk-huey/cnode/sub/sub".getBytes());
        client.setData().forPath("/zk-huey/cnodechild/sub/sub", "/zk-huey/cnode/sub/sub-new".getBytes());
        
        Thread.sleep(10 * 1000);
        pool.shutdown();
        client.close();
    }
}