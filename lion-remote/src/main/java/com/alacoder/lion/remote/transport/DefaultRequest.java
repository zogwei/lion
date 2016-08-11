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

import java.util.Map;

/**
 * @ClassName: Request
 * @Description: TODO
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
	
	
	/**
	 * getter method
	 * @return the id
	 */
	
	public Long getId() {
		return id;
	}
	/**
	 * setter method
	 * @param id the id to set
	 */
	
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * getter method
	 * @return the parametersDesc
	 */
	
	public String getParametersDesc() {
		return parametersDesc;
	}
	/**
	 * setter method
	 * @param parametersDesc the parametersDesc to set
	 */
	
	public void setParametersDesc(String parametersDesc) {
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
	
	public String getMethod() {
		return method;
	}
	/**
	 * setter method
	 * @param method the method to set
	 */
	
	public void setMethod(String method) {
		this.method = method;
	}
	/*
	  * <p>Title: toString</p>
	  * <p>Description: </p>
	  * @return
	  * @see java.lang.Object#toString()
	  */
	
	
	@Override
	public String toString() {
		return "Request [interfaceName=" + interfaceName + ", method=" + method + "]";
	}
	
	
	
}
