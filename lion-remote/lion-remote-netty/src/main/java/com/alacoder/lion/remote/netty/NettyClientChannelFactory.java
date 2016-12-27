/**
 * 版权声明：lion 版权所有 违者必究 2016
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
import com.alacoder.lion.remote.AbstractPoolClient;
import com.alacoder.lion.remote.Channel;

/**
 * @ClassName: NettyChannelFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 上午11:08:53
 *
 */

public class NettyClientChannelFactory extends BasePoolableObjectFactory<Channel> {
	private String factoryName = "";
	private AbstractPoolClient nettyClient;

	public NettyClientChannelFactory(AbstractPoolClient nettyClient) {
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
	public Channel makeObject() throws Exception {

		return nettyClient.getChannel();
	}

	@Override
	public void destroyObject(final Channel obj) throws Exception {
		if (obj instanceof Channel) {
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
	public void activateObject(Channel obj) throws Exception {
		if (obj instanceof NettyChannel) {
			final NettyChannel client = (NettyChannel) obj;
			if (!client.isAvailable()) {
				client.open();
			}
		}
	}

	@Override
	public void passivateObject(Channel obj) throws Exception {
		
	}

	@Override
	public boolean validateObject(final Channel obj) {
		if (obj instanceof NettyChannel) {
			final Channel client = (Channel) obj;
			try {
				return client.isAvailable();
			} catch (final Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}


}