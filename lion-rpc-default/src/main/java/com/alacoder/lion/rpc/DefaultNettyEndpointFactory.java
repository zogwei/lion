/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: DefaultNettyEndpointFactory.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 上午10:57:30
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.remote.Client;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.Server;
import com.alacoder.lion.remote.netty.NettyClient;
import com.alacoder.lion.remote.netty.NettyServer;

/**
 * @ClassName: DefaultNettyEndpointFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 上午10:57:30
 *
 */
@SpiMeta(name = "lion")
public class DefaultNettyEndpointFactory extends AbstractEndpointFactory {

	public Client createClient(LionURL url, MessageHandler messageHandler) {
		return new NettyClient(url,messageHandler);
	}

	@Override
	protected Server innerCreateServer(LionURL url, MessageHandler messageHandler) {
		return new NettyServer(url, messageHandler);
	}

}
