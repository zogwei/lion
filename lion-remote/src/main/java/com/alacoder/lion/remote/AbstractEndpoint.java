/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: AbstractEndpoint.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月13日 下午4:49:44
 * @version V1.0
 */

package com.alacoder.lion.remote;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.LoggerUtil;

/**
 * @ClassName: AbstractEndpoint
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月13日 下午4:49:44
 *
 */

public abstract class AbstractEndpoint implements Endpoint {
	
    protected volatile EndpointState state = EndpointState.UNINIT;
	
	// 连续失败次数
	private AtomicLong errorCount = new AtomicLong(0);
	// 最大连接数
	private int maxClientConnection = 0;
	
	protected LionURL url;
	
	// 异步的request，需要注册callback future
	// 触发remove的操作有： 1) service的返回结果处理。 2) timeout thread cancel
	protected ConcurrentMap<Long, ResponseFuture> callbackMap = new ConcurrentHashMap<Long, ResponseFuture>();
	
	// 回收过期任务
	private static ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
	
	protected ScheduledFuture<?> timeMonitorFuture = null;
	
	public AbstractEndpoint(LionURL url){
		this.url = url;
		maxClientConnection = url.getIntParameter(URLParamType.maxClientConnection.getName(),
				URLParamType.maxClientConnection.getIntValue());

		timeMonitorFuture = scheduledExecutor.scheduleWithFixedDelay(
				new TimeoutMonitor("timeout_monitor_" + url.getHost() + "_" + url.getPort()),
				LionConstants.NETTY_TIMEOUT_TIMER_PERIOD, LionConstants.NETTY_TIMEOUT_TIMER_PERIOD,
				TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 增加调用失败的次数：
	 * 
	 * <pre>
	 * 	 	如果连续失败的次数 >= maxClientConnection, 那么把client设置成不可用状态
	 * </pre>
	 * 
	 */
	public void incrErrorCount() {
		long count = errorCount.incrementAndGet();

		// 如果节点是可用状态，同时当前连续失败的次数超过限制maxClientConnection次，那么把该节点标示为不可用
		if (count >= maxClientConnection && state.isAliveState()) {
			synchronized (this) {
				count = errorCount.longValue();

				if (count >= maxClientConnection && state.isAliveState()) {
					LoggerUtil.error("NettyClient unavailable Error: url=" + url.getIdentity() + " "
							+ url.getServerPortStr());
					state = EndpointState.UNALIVE;
				}
			}
		}
	}
	
	/**
	 * 重置调用失败的计数 ：
	 * 
	 * <pre>
	 * 把节点设置成可用
	 * </pre>
	 * 
	 */
	public void resetErrorCount() {
		errorCount.set(0);

		if (state.isAliveState()) {
			return;
		}

		synchronized (this) {
			if (state.isAliveState()) {
				return;
			}

			// 如果节点是unalive才进行设置，而如果是 close 或者 uninit，那么直接忽略
			if (state.isUnAliveState()) {
				long count = errorCount.longValue();

				// 过程中有其他并发更新errorCount的，因此这里需要进行一次判断
				if (count < maxClientConnection) {
					state = EndpointState.ALIVE;
					LoggerUtil.info("NettyClient recover available: url=" + url.getIdentity() + " "
							+ url.getServerPortStr());
				}
			}
		}
	}
	


	public void registerCallback(long requestId, ResponseFuture nettyResponseFuture) {
		if (this.callbackMap.size() >= LionConstants.NETTY_CLIENT_MAX_REQUEST) {
			// reject request, prevent from OutOfMemoryError
			throw new LionServiceException("NettyClient over of max concurrent request, drop request, url: "
					+ url.getUri() + " requestId=" + requestId, LionErrorMsgConstant.SERVICE_REJECT);
		}

		this.callbackMap.put(requestId, nettyResponseFuture);
	}

	/**
	 * 移除回调的response
	 * 
	 * @param requestId
	 * @return
	 */
	public ResponseFuture removeCallback(long requestId) {
		return callbackMap.remove(requestId);
	}
	/**
	 * 回收超时任务
	 * 
	 * @author maijunsheng
	 * 
	 */
	class TimeoutMonitor implements Runnable {
		private String name;

		public TimeoutMonitor(String name) {
			this.name = name;
		}

		public void run() {

			long currentTime = System.currentTimeMillis();

			for (Map.Entry<Long, ResponseFuture> entry : callbackMap.entrySet()) {
				try {
					ResponseFuture future = entry.getValue();

					if (future.getCreateTime() + future.getTimeout() < currentTime) {
						// timeout: remove from callback list, and then cancel
						removeCallback(entry.getKey());
						future.cancel();
					} 
				} catch (Exception e) {
					LoggerUtil.error(name + " clear timeout future Error: uri=" + url.getUri() + " requestId=" + entry.getKey(), e);
				}
			}
		}
	}
	
	public void close(){
		scheduledExecutor.shutdownNow();
		try {
			scheduledExecutor.awaitTermination(1000,  TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			LoggerUtil.error("close scheduledExecutor error ", e);
		}
	}

}
