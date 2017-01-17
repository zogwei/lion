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

package com.alacoder.lion.rpc.remote;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alacoder.lion.remote.codec.RemoteProtocolVersion;
import com.alacoder.lion.remote.transport.DefaultRequest;

/**
 * @ClassName: Request
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:08:38
 *
 */

public class DefaultRpcRequest extends DefaultRequest<RpcRequestInfo> {
	
	private Long id;
	private RpcRequestInfo rpcRequestInfo;

	@Override
	public String getIdentity() {
//		methodName + "(" + paramDesc + ")."
		return rpcRequestInfo.getMethodName()  + "(" + rpcRequestInfo.getParamtersDesc() + ").";
	}

	@Override
	public void setRequestMsg(RpcRequestInfo t) {
		this.rpcRequestInfo = t;
	}

	@Override
	public RpcRequestInfo getRequestMsg() {
		return rpcRequestInfo;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

}
