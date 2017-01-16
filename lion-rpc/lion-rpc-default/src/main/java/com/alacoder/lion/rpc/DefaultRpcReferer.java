/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-default
 * @Title: DefaultRpcReferer.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月10日 下午2:37:59
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import com.alacoder.common.exception.LionServiceException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.remote.Client;
import com.alacoder.lion.remote.TransportException;
import com.alacoder.lion.rpc.remote.RpcRequest;
import com.alacoder.lion.rpc.remote.RpcRequestInfo;
import com.alacoder.lion.rpc.remote.RpcResponse;

/**
 * @ClassName: DefaultRpcReferer
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月10日 下午2:37:59
 *
 */

public class DefaultRpcReferer<T> extends AbstractReferer<T>{

	private final static LogService logger = LogFactory.getLogService(DefaultRpcReferer.class);
	
	private Client client;
    private EndpointFactory endpointFactory;

	 public DefaultRpcReferer(Class<T> clz, LionURL url, LionURL serviceUrl) {
         super(clz, url, serviceUrl);

         endpointFactory =
                 ExtensionLoader.getExtensionLoader(EndpointFactory.class).getExtension(
                         url.getParameter(URLParamType.endpointFactory.getName(), URLParamType.endpointFactory.getValue()));

         client = endpointFactory.createClient(url,null);
     }

     @Override
     protected RpcResponse doCall(RpcRequest request) {
     	RpcRequestInfo rpcRequestInfo =  request.getRequestMsg();
         try {
             // 为了能够实现跨group请求，需要使用server端的group。
        	 rpcRequestInfo.setAttachment(URLParamType.group.getName(), serviceUrl.getGroup());
        	 
             return (RpcResponse)client.request(request);
         } catch (TransportException exception) {
             throw new LionServiceException("DefaultRpcReferer call Error: url=" + url.getUri(), exception);
         }
     }

     @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
     protected void decrActiveCount(RpcRequest request, RpcResponse response) {
         if (response == null || !(response instanceof Future)) {
             activeRefererCount.decrementAndGet();
             return;
         }

         Future future = (Future) response;

         future.addListener(new FutureListener() {
             @Override
             public void operationComplete(Future future) throws Exception {
                 activeRefererCount.decrementAndGet();
             }
         });
     }

     @Override
     protected boolean doInit() {
         boolean result = client.open();

         return result;
     }

     @Override
     public boolean isAvailable() {
         return client.isAvailable();
     }

     @Override
     public void destroy() {
         endpointFactory.safeReleaseResource(client, url);
         logger.info("DefaultRpcReferer destory client: url={}" + url);
     }
 }

