/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-api
 * @Title: DefaultResponse.java
 * @Package com.alacoder.lion.remote.transport
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月10日 下午5:48:11
 * @version V1.0
 */

package com.alacoder.lion.remote.transport;

import java.util.Collections;
import java.util.Map;

import com.alacoder.lion.remote.codec.RemoteProtocolVersion;


/**
 * @ClassName: DefaultResponse
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月10日 下午5:48:11
 *
 */

public class DefaultResponse<T> implements Response<T> {
	
	private static final long serialVersionUID = 1L;
	private T value;
    private Exception exception;
    private Long id;
    private long processTime;
    private int timeout;
    
    private Long requestId;
    
    private Map<String, String> attachments;// rpc协议版本兼容时可以回传一些额外的信息
    
    private long createTime = System.currentTimeMillis();
    
    private byte rpcProtocolVersion = RemoteProtocolVersion.VERSION_1.getVersion();

    public DefaultResponse(Response<T> response) {
        this.value = response.getValue();
        this.exception = response.getException();
        this.id = response.getRequestId();
        this.processTime = response.getProcessTime();
        this.timeout = response.getTimeout();
    }
    
    public DefaultResponse() {

    }
    
    public DefaultResponse(T value) {
        this.value = value;
    }

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getProcessTime() {
		return processTime;
	}

	public void setProcessTime(long processTime) {
		this.processTime = processTime;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	@Override
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	   @SuppressWarnings("unchecked")
	    public Map<String, String> getAttachments() {
	        return attachments != null ? attachments : Collections.EMPTY_MAP;
	    }

	    @Override
	    public void setAttachment(String key, String value) {
	        if (this.attachments == null) {
	            this.attachments = new java.util.HashMap<String, String>();
	        }

	        this.attachments.put(key, value);
	    }

	    public void setAttachments(Map<String, String> attachments) {
	        this.attachments = attachments;
	    }

	    public byte getRpcProtocolVersion() {
	        return rpcProtocolVersion;
	    }

	    public void setRpcProtocolVersion(byte rpcProtocolVersion) {
	        this.rpcProtocolVersion = rpcProtocolVersion;
	    }
}
