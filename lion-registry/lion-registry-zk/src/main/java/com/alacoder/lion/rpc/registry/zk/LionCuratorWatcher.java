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

import java.util.ArrayList;
import java.util.List;

import com.alacoder.lion.registry.NotifyListener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: LionCuratorWatcher
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月13日 下午7:06:20
 *
 */

public class LionCuratorWatcher  {
	
	private final static LogService logger = LogFactory.getLogService(LionCuratorWatcher.class);
	
	NotifyListener listener = null;
	LionURL url = null;
	CuratorOper zkOper  = null;
	public LionCuratorWatcher(NotifyListener listener,LionURL url,CuratorOper zkOper ){
		this.listener = listener;
		this.url = url;
		this.zkOper = zkOper;
	}

	public void process(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
		if(listener != null){
			String childPath = event.getData().getPath();
			String path = childPath.substring(0,childPath.lastIndexOf("/"));
			List<LionURL> urls = new ArrayList<LionURL>();
			List<String> childKeys = zkOper.getChildrenKeys(path);
			if(childKeys!=null && childKeys.size() >0){
				for (String node : childKeys) {
	                String nodePath = path+ LionConstants.PATH_SEPARATOR + node;
	                String data = zkOper.getDirectly(nodePath);
	                try {
	                	LionURL url = LionURL.valueOf(data);
	                    urls.add(url);
	                } catch (Exception e) {
	                    logger.warn(String.format("Found malformed urls from ZookeeperRegistry, path=%s", nodePath), e);
	                }
	            }
			}
			listener.notify(url, urls);
		}
	}

	public NotifyListener getListener() {
		return listener;
	}

	public void setListener(NotifyListener listener) {
		this.listener = listener;
	}

}
