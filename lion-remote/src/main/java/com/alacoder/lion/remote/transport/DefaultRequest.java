/**
 * 版权声明：bee 版权所有 违者必究 2016
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alacoder.lion.remote.codec.RemoteProtocolVersion;

/**
 * @ClassName: Request
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:08:38
 *
 */

public class DefaultRequest implements Request{

	private Long id;
	private String interfaceName;
	private String method;
	private String parametersDesc;
	private Object[] arguments;
	private Map<String, String> attachments;
	private int retrey;
	
    private byte rpcProtocolVersion = RemoteProtocolVersion.VERSION_1.getVersion();
	
	/**
	 * getter method
	 * @return the id
	 */
	
	public Long getRequestId() {
		return id;
	}
	/**
	 * setter method
	 * @param id the id to set
	 */
	
	public void setRequestId(Long id) {
		this.id = id;
	}
	/**
	 * getter method
	 * @return the parametersDesc
	 */
	
	public String getParamtersDesc() {
		return parametersDesc;
	}
	/**
	 * setter method
	 * @param parametersDesc the parametersDesc to set
	 */
	
	public void setParamtersDesc(String parametersDesc) {
		this.parametersDesc = parametersDesc;
	}
	/**
	 * getter method
	 * @return the arguments
	 */
	
	public Object[] getArguments() {
		return arguments;
	}
	/**
	 * setter method
	 * @param arguments the arguments to set
	 */
	
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
	/**
	 * getter method
	 * @return the attachments
	 */
	
	public Map<String, String> getAttachments() {
		return attachments;
	}
	/**
	 * setter method
	 * @param attachments the attachments to set
	 */
	
	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}
	/**
	 * getter method
	 * @return the interfaceName
	 */
	
	public String getInterfaceName() {
		return interfaceName;
	}
	/**
	 * setter method
	 * @param interfaceName the interfaceName to set
	 */
	
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	/**
	 * getter method
	 * @return the method
	 */
	
	public String getMethodName() {
		return method;
	}
	/**
	 * setter method
	 * @param method the method to set
	 */
	
	public void setMethodName(String method) {
		this.method = method;
	}
	/*
	  * <p>Title: toString</p>
	  * <p>Description: </p>
	  * @return
	  * @see java.lang.Object#toString()
	  */
	@Override
	public void setRpcProtocolVersion(byte rpcProtocolVersion) {
		this.rpcProtocolVersion = rpcProtocolVersion;
	}
	@Override
	public byte getRpcProtocolVersion() {
		return rpcProtocolVersion;
	}
	@Override
	public String toString() {
		return "DefaultRequest [id=" + id + ", interfaceName=" + interfaceName
				+ ", method=" + method + ", parametersDesc=" + parametersDesc
				+ ", arguments=" + Arrays.toString(arguments)
				+ ", attachments=" + attachments + ", rpcProtocolVersion="
				+ rpcProtocolVersion + ", getId()=" + getRequestId()
				+ ", getParamtersDesc()=" + getParamtersDesc()
				+ ", getArguments()=" + Arrays.toString(getArguments())
				+ ", getAttachments()=" + getAttachments()
				+ ", getInterfaceName()=" + getInterfaceName()
				+ ", getMethodName()=" + getMethodName()
				+ ", getRpcProtocolVersion()=" + getRpcProtocolVersion()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	@Override
	public int getRetries() {
		return this.retrey;
	}
	@Override
    public void setAttachment(String key, String value) {
        if (this.attachments == null) {
            this.attachments = new HashMap<String, String>();
        }

        this.attachments.put(key, value);
    }
	@Override
	public void setRetries(int i) {
		this.retrey = i;
	}
	
	
}
