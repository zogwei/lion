/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyChannelFactory.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 上午11:08:53
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;

import org.apache.commons.pool.BasePoolableObjectFactory;

import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.utils.LoggerUtil;

/**
 * @ClassName: NettyChannelFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 上午11:08:53
 *
 */

public class NettyClientChannelFactory extends BasePoolableObjectFactory {
	private String factoryName = "";
	private NettyClient nettyClient;

	public NettyClientChannelFactory(NettyClient nettyClient) {
		super();

		this.nettyClient = nettyClient;
		this.factoryName = "NettyChannelFactory_" + nettyClient.getUrl().getHost() + "_"
				+ nettyClient.getUrl().getPort();
	}

	public String getFactoryName() {
		return factoryName;
	}

	@Override
	public String toString() {
		return factoryName;
	}

	@Override
	public Object makeObject() throws Exception {
		NettyChannel nettyChannel = new NettyChannel(nettyClient);
		nettyChannel.open();

		return nettyChannel;
	}

	@Override
	public void destroyObject(final Object obj) throws Exception {
		if (obj instanceof NettyChannel) {
			NettyChannel client = (NettyChannel) obj;
			LionURL url = nettyClient.getUrl();

			try {
				client.close();

				LoggerUtil.info(factoryName + " client disconnect Success: " + url.getUri());
			} catch (Exception e) {
				LoggerUtil.error(factoryName + " client disconnect Error: " + url.getUri(), e);
			}
		}
	}

	@Override
	public boolean validateObject(final Object obj) {
		if (obj instanceof NettyChannel) {
			final NettyChannel client = (NettyChannel) obj;
			try {
				return client.isAvailable();
			} catch (final Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public void activateObject(Object obj) throws Exception {
		if (obj instanceof NettyChannel) {
			final NettyChannel client = (NettyChannel) obj;
			if (!client.isAvailable()) {
				client.open();
			}
		}
	}

	@Override
	public void passivateObject(Object obj) throws Exception {
		
	}
}