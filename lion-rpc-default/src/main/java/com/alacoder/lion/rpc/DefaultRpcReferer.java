/**
 * 版权声明：bee 版权所有 违者必究 2016
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
import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.url.URL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.Client;
import com.alacoder.lion.remote.TransportException;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: DefaultRpcReferer
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月10日 下午2:37:59
 *
 */

public class DefaultRpcReferer<T> extends AbstractReferer<T>{
	private Client client;
    private EndpointFactory endpointFactory;

	 public DefaultRpcReferer(Class<T> clz, URL url, URL serviceUrl) {
         super(clz, url, serviceUrl);

         endpointFactory =
                 ExtensionLoader.getExtensionLoader(EndpointFactory.class).getExtension(
                         url.getParameter(URLParamType.endpointFactory.getName(), URLParamType.endpointFactory.getValue()));

         client = endpointFactory.createClient(url,null);
     }

     @Override
     protected Response doCall(Request request) {
         try {
             // 为了能够实现跨group请求，需要使用server端的group。
             request.setAttachment(URLParamType.group.getName(), serviceUrl.getGroup());
             return client.request(request);
         } catch (TransportException exception) {
             throw new LionServiceException("DefaultRpcReferer call Error: url=" + url.getUri(), exception);
         }
     }

     @Override
     protected void decrActiveCount(Request request, Response response) {
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
         LoggerUtil.info("DefaultRpcReferer destory client: url={}" + url);
     }
 }

