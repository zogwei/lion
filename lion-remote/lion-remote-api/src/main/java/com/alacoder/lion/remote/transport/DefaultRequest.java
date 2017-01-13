/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-api
 * @Title: DefaultRequest.java
 * @Package com.alacoder.lion.remote.transport
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月10日 下午5:47:40
 * @version V1.0
 */

package com.alacoder.lion.remote.transport;

/**
 * @ClassName: DefaultRequest
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月10日 下午5:47:40
 *
 */

@SuppressWarnings("serial")
public class DefaultRequest<T> implements Request<T> {
	private Long id;
	private T t;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getIdentity() {
		return String.valueOf(id);
	}

	@Override
	public void setRequestMsg(T t) {
		this.t = t;
	}
	
	public T getRequestMsg() {
		return t;
	}

}
