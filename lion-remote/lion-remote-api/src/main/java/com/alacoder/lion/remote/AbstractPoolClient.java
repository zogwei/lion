/**
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
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;

/**
 * @ClassName: AbstractPoolClient
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 上午11:03:28
 *
 */

public abstract class AbstractPoolClient extends AbstractClient{
	
	private final static LogService logger = LogFactory.getLogService(AbstractPoolClient.class);
	
	
    protected static long defaultMinEvictableIdleTimeMillis = (long) 1000 * 60 * 60;//默认链接空闲时间
    protected static long defaultSoftMinEvictableIdleTimeMillis = (long) 1000 * 60 * 10;//
    protected static long defaultTimeBetweenEvictionRunsMillis = (long) 1000 * 60 * 10;//默认回收周期
    @SuppressWarnings("rawtypes")
	protected GenericObjectPool pool;
    protected GenericObjectPool.Config poolConfig;
    @SuppressWarnings("rawtypes")
	protected PoolableObjectFactory factory;
	

	public AbstractPoolClient(LionURL url, MessageHandler messageHandler) {
		super(url, messageHandler);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
        
        pool = new GenericObjectPool(factory, poolConfig);

        boolean lazyInit = url.getBooleanParameter(URLParamType.lazyInit.getName(), URLParamType.lazyInit.getBooleanValue());

        if (!lazyInit) {
            for (int i = 0; i < poolConfig.minIdle; i++) {
                try {
                    pool.addObject();
                } catch (Exception e) {
                    logger.error("NettyClient init pool create connect Error: url=" + url.getUri(), e);
                }
            }   
        }
	}
	
	protected void closePool() {
        try {
			pool.close();
		} catch (Exception e) {
			 logger.error("pool close error", e);
		}
	}
	
    @SuppressWarnings("rawtypes")
	protected abstract BasePoolableObjectFactory createChannelFactory();
    
    public abstract Channel getChannel() ;
    
    protected Channel borrowObject() throws Exception {
        Channel nettyChannel = (Channel) pool.borrowObject();

        if (nettyChannel != null && nettyChannel.isAvailable()) {
            return nettyChannel;
        }

        invalidateObject(nettyChannel);

        String errorMsg = this.getClass().getSimpleName() + " borrowObject Error: url=" + url.getUri();
        logger.error(errorMsg);
        throw new LionServiceException(errorMsg);
    }
    
    @SuppressWarnings("unchecked")
	protected void returnObject(Channel channel) {
        if (channel == null) {
            return;
        }

        try {
            pool.returnObject(channel);
        } catch (Exception ie) {
            logger.error(this.getClass().getSimpleName() + " return client Error: url=" + url.getUri(), ie);
        }
    }

    @SuppressWarnings("unchecked")
	protected void invalidateObject(Channel nettyChannel) {
        if (nettyChannel == null) {
            return;
        }
        try {
            pool.invalidateObject(nettyChannel);
        } catch (Exception ie) {
            logger.error(this.getClass().getSimpleName() + " invalidate client Error: url=" + url.getUri(), ie);
        }
    }


	
}
