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

/**
 * @ClassName: DefaultResponse
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月10日 下午5:48:11
 *
 */

public class DefaultResponse implements Response {
	
	private static final long serialVersionUID = 1L;
	private Object value;
    private Exception exception;
    private long id;
    private long processTime;
    private int timeout;
    private long createTime = System.currentTimeMillis();
    
    public DefaultResponse(Response response) {
        this.value = response.getValue();
        this.exception = response.getException();
        this.id = response.getRequestId();
        this.processTime = response.getProcessTime();
        this.timeout = response.getTimeout();
    }
    
    public DefaultResponse() {

    }
    
    public DefaultResponse(Object value) {
        this.value = value;
    }

	@Override
	public Long getRequestId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRequestId(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getCreateTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Exception getException() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getProcessTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setProcessTime(long time) {
		// TODO Auto-generated method stub
		
	}

}
