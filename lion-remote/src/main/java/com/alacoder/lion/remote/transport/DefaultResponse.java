/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: Request.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:08:38
 * @version V1.0
 */

package com.alacoder.lion.remote.transport;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.alacoder.common.exception.LionServiceException;
import com.alacoder.lion.remote.codec.RemoteProtocolVersion;

/**
 * @ClassName: Request
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:08:38
 *
 */

public class DefaultResponse implements Response{

    private Object value;
    private Exception exception;
    private long id;
    private long processTime;
    private int timeout;
    private long createTime = System.currentTimeMillis();
    
	
    private Map<String, String> attachments;// rpc协议版本兼容时可以回传一些额外的信息

    private byte rpcProtocolVersion = RemoteProtocolVersion.VERSION_1.getVersion();

    public DefaultResponse() {}

    public DefaultResponse(long id) {
        this.id = id;
    }

    public DefaultResponse(Response response) {
        this.value = response.getValue();
        this.exception = response.getException();
        this.id = response.getRequestId();
        this.processTime = response.getProcessTime();
        this.timeout = response.getTimeout();
    }

    public DefaultResponse(Object value) {
        this.value = value;
    }

    public DefaultResponse(Object value, long requestId) {
        this.value = value;
    }

    public Object getValue() {
        if (exception != null) {
            throw (exception instanceof RuntimeException) ? (RuntimeException) exception : new LionServiceException(
                    exception.getMessage(), exception);
        }

        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Long getRequestId() {
        return id;
    }

    public void setRequestId(Long id) {
        this.id = id;
    }

    @Override
    public long getProcessTime() {
        return processTime;
    }

    @Override
    public void setProcessTime(long time) {
        this.processTime = time;
    }

    public int getTimeout() {
        return this.timeout;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getAttachments() {
        return attachments != null ? attachments : Collections.EMPTY_MAP;
    }

    @Override
    public void setAttachment(String key, String value) {
        if (this.attachments == null) {
            this.attachments = new HashMap<String, String>();
        }

        this.attachments.put(key, value);
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    @Override
    public byte getRpcProtocolVersion() {
        return rpcProtocolVersion;
    }

    public void setRpcProtocolVersion(byte rpcProtocolVersion) {
        this.rpcProtocolVersion = rpcProtocolVersion;
    }

	@Override
	public String toString() {
		return "DefaultResponse [value=" + value + ", exception=" + exception
				+ ", id=" + id + ", processTime=" + processTime + ", timeout="
				+ timeout + ", attachments=" + attachments
				+ ", rpcProtocolVersion=" + rpcProtocolVersion
				+ ", getValue()=" + getValue() + ", getException()="
				+ getException() + ", getId()=" + getRequestId()
				+ ", getProcessTime()=" + getProcessTime() + ", getTimeout()="
				+ getTimeout() + ", getAttachments()=" + getAttachments()
				+ ", getRpcProtocolVersion()=" + getRpcProtocolVersion()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	@Override
	public long getCreateTime() {
		return this.createTime;
	}

}
