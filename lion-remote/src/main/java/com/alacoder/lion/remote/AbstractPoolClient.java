/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: AbstractPoolClient.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 上午11:03:28
 * @version V1.0
 */

package com.alacoder.lion.remote;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.alacoder.common.exception.LionServiceException;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.LoggerUtil;

/**
 * @ClassName: AbstractPoolClient
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 上午11:03:28
 *
 */

public abstract class AbstractPoolClient extends AbstractClient{
    protected static long defaultMinEvictableIdleTimeMillis = (long) 1000 * 60 * 60;//默认链接空闲时间
    protected static long defaultSoftMinEvictableIdleTimeMillis = (long) 1000 * 60 * 10;//
    protected static long defaultTimeBetweenEvictionRunsMillis = (long) 1000 * 60 * 10;//默认回收周期
    protected GenericObjectPool pool;
    protected GenericObjectPool.Config poolConfig;
    protected PoolableObjectFactory factory;
	

	public AbstractPoolClient(LionURL url, MessageHandler messageHandler) {
		super(url, messageHandler);
	}

	protected void initPool() {
        poolConfig = new GenericObjectPool.Config();
        poolConfig.minIdle =
                url.getIntParameter(URLParamType.minClientConnection.getName(), URLParamType.minClientConnection.getIntValue());
        poolConfig.maxIdle =
                url.getIntParameter(URLParamType.maxClientConnection.getName(), URLParamType.maxClientConnection.getIntValue());
        poolConfig.maxActive = poolConfig.maxIdle;
        poolConfig.maxWait = url.getIntParameter(URLParamType.requestTimeout.getName(), URLParamType.requestTimeout.getIntValue());
        poolConfig.lifo = url.getBooleanParameter(URLParamType.poolLifo.getName(), URLParamType.poolLifo.getBooleanValue());
        poolConfig.minEvictableIdleTimeMillis = defaultMinEvictableIdleTimeMillis;
        poolConfig.softMinEvictableIdleTimeMillis = defaultSoftMinEvictableIdleTimeMillis;
        poolConfig.timeBetweenEvictionRunsMillis = defaultTimeBetweenEvictionRunsMillis;
        factory = createChannelFactory();
	}
	
    protected abstract BasePoolableObjectFactory createChannelFactory();
    
    protected Channel borrowObject() throws Exception {
        Channel nettyChannel = (Channel) pool.borrowObject();

        if (nettyChannel != null && nettyChannel.isAvailable()) {
            return nettyChannel;
        }

        invalidateObject(nettyChannel);

        String errorMsg = this.getClass().getSimpleName() + " borrowObject Error: url=" + url.getUri();
        LoggerUtil.error(errorMsg);
        throw new LionServiceException(errorMsg);
    }

    protected void invalidateObject(Channel nettyChannel) {
        if (nettyChannel == null) {
            return;
        }
        try {
            pool.invalidateObject(nettyChannel);
        } catch (Exception ie) {
            LoggerUtil.error(this.getClass().getSimpleName() + " invalidate client Error: url=" + url.getUri(), ie);
        }
    }

    protected void returnObject(Channel channel) {
        if (channel == null) {
            return;
        }

        try {
            pool.returnObject(channel);
        } catch (Exception ie) {
            LoggerUtil.error(this.getClass().getSimpleName() + " return client Error: url=" + url.getUri(), ie);
        }
    }
	
}
