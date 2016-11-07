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

import java.util.Map;

import com.alacoder.lion.remote.TransportData;

/**
 * @ClassName: Request
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:08:38
 *
 */

public interface Request extends TransportData{

	public String getParamtersDesc();;
	
	public void setParamtersDesc(String parametersDesc);
	
	public Object[] getArguments();
	
	public void setArguments(Object[] arguments) ;
	
	public Map<String, String> getAttachments() ;
	
	public void setAttachments(Map<String, String> attachments) ;
	
	public void setAttachment(String name, String value);
	
	public String getInterfaceName();
	
	public void setInterfaceName(String interfaceName);
	
	public String getMethodName() ;
	
	public void setMethodName(String method);
	
    void setRpcProtocolVersion(byte rpcProtocolVersion);

    byte getRpcProtocolVersion();
    
    int getRetries();

	 void setRetries(int i);
}
