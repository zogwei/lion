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

	public DefaultRpcExporter(Provider<T> provider, LionURL url) {
		super(provider, url);
		DefaultMessageHandler messageHandler = new DefaultMessageHandler();
		endpointFactory =
                 ExtensionLoader.getExtensionLoader(EndpointFactory.class).getExtension(
                         url.getParameter(URLParamType.endpointFactory.getName(), URLParamType.endpointFactory.getValue()));
        server = endpointFactory.createServer(url, messageHandler);
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
