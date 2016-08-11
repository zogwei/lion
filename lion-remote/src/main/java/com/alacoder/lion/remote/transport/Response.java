/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: Response.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:08:53
 * @version V1.0
 */

package com.alacoder.lion.remote.transport;

import java.util.Map;

/**
 * @ClassName: Response
 * @Description: TODO
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:08:53
 *
 */

public interface Response extends TransportData{

public String getParametersDesc();;
	
	public void setParametersDesc(String parametersDesc);
	
	public Object[] getArguments();
	
	public void setArguments(Object[] arguments) ;
	
	public Map<String, String> getAttachments() ;
	
	public void setAttachments(Map<String, String> attachments) ;
	
	public String getInterfaceName();
	
	public void setInterfaceName(String interfaceName);
	
	public String getMethod() ;
	
	public void setMethod(String method);
}
