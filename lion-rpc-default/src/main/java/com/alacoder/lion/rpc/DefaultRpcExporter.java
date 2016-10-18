/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: DefaultRpcExporter.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 下午2:06:38
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.util.Map;

import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.remote.Server;

/**
 * @ClassName: DefaultRpcExporter
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 下午2:06:38
 *
 */

public class DefaultRpcExporter<T> extends AbstractExporter<T> {
	private Server server;
	private EndpointFactory endpointFactory;
	private Map<String, DefaultMessageHandler> ipPort2RequestRouter;

	public DefaultRpcExporter(Provider<T> provider, LionURL url,  Map<String, DefaultMessageHandler> ipPort2RequestRouter) {
		super(provider, url);
		DefaultMessageHandler messageHandler = getDefaultMessageHandler(ipPort2RequestRouter);
		endpointFactory =
                 ExtensionLoader.getExtensionLoader(EndpointFactory.class).getExtension(
                         url.getParameter(URLParamType.endpointFactory.getName(), URLParamType.endpointFactory.getValue()));
        server = endpointFactory.createServer(url, messageHandler);
	}
	
	public DefaultMessageHandler getDefaultMessageHandler(Map<String, DefaultMessageHandler> ipPort2RequestRouter){
		DefaultMessageHandler requestRouter = null;
          String ipPort = url.getServerPortStr();

          synchronized (ipPort2RequestRouter) {
              requestRouter = ipPort2RequestRouter.get(ipPort);

              if (requestRouter == null) {
                  requestRouter = new DefaultMessageHandler(provider);
                  ipPort2RequestRouter.put(ipPort, requestRouter);
              } else {
                  requestRouter.addProvider(provider);
              }
          }

          return requestRouter;
	}

	@Override
	public void unexport() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean doInit() {
		   boolean result = server.open();

           return result;
	}

}
