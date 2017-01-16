/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: ResponseFuture.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月13日 下午4:50:51
 * @version V1.0
 */

package com.alacoder.lion.remote;

import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: ResponseFuture
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月13日 下午4:50:51
 *
 */

@SuppressWarnings("rawtypes")
public abstract class ResponseFuture implements Future, Response {

	private static final long serialVersionUID = 1L;

	public abstract void onSuccess(Response<?> response) ;

	public abstract void onFailure(Response<?> response);
}
