/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: LionCuratorWatcher.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月13日 下午7:06:20
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry.zk;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.rpc.registry.NotifyListener;

/**
 * @ClassName: LionCuratorWatcher
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月13日 下午7:06:20
 *
 */

public class LionCuratorWatcher implements CuratorWatcher {
	
	NotifyListener listener = null;
	LionURL url = null;
	CuratorOper zkOper  = null;
	public LionCuratorWatcher(NotifyListener listener,LionURL url,CuratorOper zkOper ){
		this.listener = listener;
		this.url = url;
		this.zkOper = zkOper;
	}

	@Override
	public void process(WatchedEvent event) throws Exception {
		String parentPath = event.getPath().substring(0,event.getPath().lastIndexOf("/"));
		if(listener != null){
			listener.notify(url, LionURL.valueOf(zkOper.getChildrenKeys(parentPath)));
		}
	}

	public NotifyListener getListener() {
		return listener;
	}

	public void setListener(NotifyListener listener) {
		this.listener = listener;
	}

}
