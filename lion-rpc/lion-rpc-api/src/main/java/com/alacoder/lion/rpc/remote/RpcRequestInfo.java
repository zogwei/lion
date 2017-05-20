/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-api
 * @Title: RpcRequestInfo.java
 * @Package com.alacoder.lion.rpc.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月13日 下午3:36:15
 * @version V1.0
 */

package com.alacoder.lion.rpc.remote;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alacoder.lion.remote.codec.RemoteProtocolVersion;

/**
 * @ClassName: RpcRequestInfo
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月13日 下午3:36:15
 *
 */

@SuppressWarnings("serial")
public class RpcRequestInfo implements java.io.Serializable {

	private String interfaceName;
	private String methodName;
	private String paramtersDesc;
	private Object[] arguments;
	private Map<String, String> attachments;
	private int retrey;
	
    private byte rpcProtocolVersion = RemoteProtocolVersion.VERSION_1.getVersion();

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public Map<String, String> getAttachments() {
		return attachments;
	}

	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}

	public int getRetrey() {
		return retrey;
	}

	public void setRetrey(int retrey) {
		this.retrey = retrey;
	}

	public byte getRpcProtocolVersion() {
		return rpcProtocolVersion;
	}

	public void setRpcProtocolVersion(byte rpcProtocolVersion) {
		this.rpcProtocolVersion = rpcProtocolVersion;
	}

	public String getParamtersDesc() {
		return paramtersDesc;
	}

	public void setParamtersDesc(String paramtersDesc) {
		this.paramtersDesc = paramtersDesc;
	}
	
	//TODO attachments 并发跟新
	public void setAttachment(String name, String valueOf) {
		if(attachments == null){
			attachments = new ConcurrentHashMap<String,String >();
		}
		attachments.put(name, valueOf);
	}

	public int getRetries() {
		return retrey;
	}

	public void setRetries(int i) {
		this.retrey = i;
	}
}
