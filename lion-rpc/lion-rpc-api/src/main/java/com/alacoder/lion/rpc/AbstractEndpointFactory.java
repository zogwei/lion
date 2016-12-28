/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractEndpointFactory.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月23日 上午11:22:19
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.remote.Client;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.Server;
import com.alacoder.lion.rpc.utils.LionFrameworkUtil;

/**
 * @ClassName: AbstractEndpointFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月23日 上午11:22:19
 *
 */

public abstract class AbstractEndpointFactory implements EndpointFactory {
	
	private final static LogService logger = LogFactory.getLogService(AbstractEndpointFactory.class);

	protected Map<String, Server> ipPort2ShareServer = new HashMap<String, Server>();
	protected ConcurrentMap<Server, Set<String>> server2Url = new ConcurrentHashMap<Server, Set<String>>();
	
	public AbstractEndpointFactory(){
		
	}
	
	 @Override
	 public Server createServer(LionURL url, MessageHandler messageHandler) {
		 Server server = null;
		 synchronized(ipPort2ShareServer) {
			 String ipPort = url.getServerPortStr();
			 String protocolKey = LionFrameworkUtil.getProtocolKey(url);
			 boolean shareChannel = url.getBooleanParameter(URLParamType.shareChannel.getName(), URLParamType.shareChannel.getBooleanValue());
			 
			 if(!shareChannel) {
				 //独享端口
				 logger.info(this.getClass().getSimpleName() + " create no_share_channel server: url={}", url);
				 server = innerCreateServer(url, messageHandler);
			 }
			 
			 logger.info(this.getClass().getSimpleName() + " create share_channel server: url={}", url);
			 
			 server = ipPort2ShareServer.get(ipPort);
			 if(server != null) {
				 if (!LionFrameworkUtil.checkIfCanShallServiceChannel(server.getUrl(), url)) {
	                    throw new LionFrameworkException(
	                            "Service export Error: share channel but some config param is different, protocol or codec or serialize or maxContentLength or maxServerConnection or maxWorkerThread or heartbeatFactory, source="
	                                    + server.getUrl() + " target=" + url, LionErrorMsgConstant.FRAMEWORK_EXPORT_ERROR);
	              }
				 
				 saveEndpoint2Urls(server2Url, server, protocolKey);
				 return server;
			 }
			 
			 url = url.createCopy();
	         url.setPath(""); // 共享server端口，由于有多个interfaces存在，所以把path设置为空

	         server = innerCreateServer(url, messageHandler);
	         ipPort2ShareServer.put(ipPort, server);
	         saveEndpoint2Urls(server2Url, server, protocolKey);
	         return server;
		 }
		
	 }
	 
	private <T> void saveEndpoint2Urls(ConcurrentMap<T, Set<String>> map,T endpoint, String namespace) {
		Set<String> sets = map.get(endpoint);

		if (sets == null) {
			sets = new HashSet<String>();
			sets.add(namespace);
			map.putIfAbsent(endpoint, sets); // 规避并发问题，因为有release逻辑存在，所以这里的sets预先add了namespace
			sets = map.get(endpoint);
		}

		sets.add(namespace);
	}
	
	@Override
	public void safeReleaseResource(Server server, LionURL url) {
		// TODO Auto-generated method stub

	}

	@Override
	public void safeReleaseResource(Client client, LionURL url) {
		// TODO Auto-generated method stub

	}
	 
	 protected abstract Server innerCreateServer(LionURL url, MessageHandler messageHandler);
}
