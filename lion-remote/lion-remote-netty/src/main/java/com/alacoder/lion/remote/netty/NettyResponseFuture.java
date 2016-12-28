/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyResponseFuture.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 下午5:15:58
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.FutureListener;
import com.alacoder.lion.remote.FutureState;
import com.alacoder.lion.remote.ResponseFuture;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;
import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;

/**
 * @ClassName: NettyResponseFuture
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 下午5:15:58
 *
 */

public class NettyResponseFuture extends ResponseFuture{
	
	private final static LogService logger = LogFactory.getLogService(NettyResponseFuture.class);

	private static final long serialVersionUID = 1L;

	private volatile FutureState state = FutureState.DOING;
	
	private Object lock = new Object();
	
	private Object result = null;
	private Exception exception = null;
	
	private long createTime = System.currentTimeMillis();
	private int timeout = 0;
	private long processTime = 0;
	
	private Request request;
	private List<FutureListener> listeners;
	private Channel channel;
	
	public NettyResponseFuture(Request requestObj, int timeout, Channel channel) {
		this.request = requestObj;
		this.channel = channel;
	}

	public void onSuccess(Response response) {
		this.result = response.getValue();
		this.processTime = response.getProcessTime();

		done();
	}

	public void onFailure(Response response) {
		this.exception = response.getException();
		this.processTime = response.getProcessTime();

		done();
	}
	
	private boolean done() {
		synchronized (lock) {
			if (!isDoing()) {
				return false;
			}

			state = FutureState.DONE;
			lock.notifyAll();
		}

		notifyListeners();
		return true;
	}
	
	@Override
	public Long getRequestId() {
		return request.getRequestId();
	}

	@Override
	public void setRequestId(Long id) {
		
	}

	@Override
	public boolean cancel() {
		Exception e = new LionServiceException("NettyResponseFuture task cancel: serverPort="
				+ channel.getUrl().getServerPortStr() + " " + request + " cost="
				+ (System.currentTimeMillis() - createTime));
		return cancel(e);
	}
	
	private boolean cancel(Exception e) {
		synchronized(lock){
			if(!isDoing()) {
				return false;
			}
			state = FutureState.CANCELLED;
			exception = e;
			lock.notifyAll();
		}
		
		notifyListeners();
		return true;
	}
	
	private void notifyListeners() {
		if (listeners != null) {
			for (FutureListener listener : listeners) {
				notifyListener(listener);
			}
		}
	}
	
	private void notifyListener(FutureListener listener) {
		try {
			listener.operationComplete(this);
		} catch (Throwable t) {
			logger.error("NettyResponseFuture notifyListener Error: " + listener.getClass().getSimpleName(), t);
		}
	}
	
	private boolean isDoing(){
		return state.isDoingState();
	}

	@Override
	public boolean isCancelled() {
		return state.isCancelledState();
	}

	@Override
	public boolean isDone() {
		return state.isDoneState();
	}

	@Override
	public boolean isSuccess() {
		return isDone() && (exception == null);
	}

	@Override
	public void addListener(FutureListener listener) {
		if (listener == null) {
			throw new NullPointerException("FutureListener is null");
		}

		boolean notifyNow = false;
		synchronized (lock) {
			if (!isDoing()) {
				// is success, failure, timeout or cancel, don't add into
				// listeners, just notify
				notifyNow = true;
			} else {
				if (listeners == null) {
					listeners = new ArrayList<FutureListener>(1);
				}

				listeners.add(listener);
			}
		}

		if (notifyNow) {
			notifyListener(listener);
		}
		
	}

	@Override
	public Object getValue() {
		synchronized (lock) {
			if (!isDoing()) {
				return getValueOrThrowable();
			}

			if (timeout <= 0) {
				try {
					lock.wait();
				} catch (Exception e) {
					cancel(new LionServiceException("NettyResponseFuture getValue InterruptedException : "
							+ request + " cost="
							+ (System.currentTimeMillis() - createTime), e));
				}

				// don't need to notifylisteners, because onSuccess or
				// onFailure or cancel method already call notifylisteners
				return getValueOrThrowable();
			} else {
				long waitTime = timeout - (System.currentTimeMillis() - createTime);

				if (waitTime > 0) {
					for (;;) {
						try {
							lock.wait(waitTime);
						} catch (InterruptedException e) {
						}

						if (!isDoing()) {
							break;
						} else {
							waitTime = timeout - (System.currentTimeMillis() - createTime);
							if (waitTime <= 0) {
								break;
							}
						}
					}
				}

				if (isDoing()) {
					timeoutSoCancel();
				}
			}
			return getValueOrThrowable();
		}
	}
	
	private Object getValueOrThrowable() {
		if (exception != null) {
			throw (exception instanceof RuntimeException) ? (RuntimeException) exception : new LionServiceException(
					exception.getMessage(), exception);
		}

		return result;
	}
	
	private void timeoutSoCancel() {
		this.processTime = System.currentTimeMillis() - createTime;

		synchronized (lock) {
			if (!isDoing()) {
				return;
			}
			
			state = FutureState.CANCELLED;
			exception = new LionServiceException("NettyResponseFuture request timeout: serverPort="
					+ channel.getUrl().getServerPortStr() + " " + request + " cost="
					+ (System.currentTimeMillis() - createTime), LionErrorMsgConstant.SERVICE_TIMEOUT);
			
			lock.notifyAll();
		}

		notifyListeners();
	}

	@Override
	public Exception getException() {
		return exception;
	}

	@Override
	public long getProcessTime() {
		return processTime;
	}

	@Override
	public void setProcessTime(long time) {
		this.processTime = time;
	}

	@Override
	public int getTimeout() {
		return this.timeout;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getAttachments() {
		  // 不需要使用
        return Collections.EMPTY_MAP;
	}

	@Override
	public void setAttachment(String key, String value) {
	}

	@Override
	public void setRpcProtocolVersion(byte rpcProtocolVersion) {
		
	}

	@Override
	public byte getRpcProtocolVersion() {
		return 0;
	}
	
	public long getCreateTime() {
		return this.createTime;
	}
	
}
