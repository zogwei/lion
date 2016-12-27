/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: SessionConnectionStateListener.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月6日 上午10:47:23
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry.zk;

/**
 * @ClassName: SessionConnectionStateListener
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月6日 上午10:47:23
 *
 */

import org.apache.curator.framework.CuratorFramework;  
import org.apache.curator.framework.state.ConnectionState;  
import org.apache.curator.framework.state.ConnectionStateListener;  
import org.apache.zookeeper.CreateMode;  
  
public class SessionConnectionStateListener implements ConnectionStateListener {  
    private String zkRegPathPrefix;  
    private String regContent;  
  
    public SessionConnectionStateListener(String zkRegPathPrefix, String regContent) {  
        this.zkRegPathPrefix = zkRegPathPrefix;  
        this.regContent = regContent;  
    }  
  
    @Override  
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState){  
        if(connectionState == ConnectionState.LOST){  
            while(true){  
                try {  
                    System.err.println("我来了，嘿嘿");  
                    if(curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()){  
                        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(zkRegPathPrefix, regContent.getBytes("UTF-8"));  
                        break;  
                    }  
                } catch (InterruptedException e) {  
                    break;  
                } catch (Exception e){  
                      
                }  
            }  
        }  
    }     
}  