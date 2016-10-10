/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: TransportData.java
 * @Package com.alacoder.lion.remote.transport
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:13:09
 * @version V1.0
 */

package com.alacoder.lion.remote;

/**
 * @ClassName: TransportData
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:13:09
 *
 */

public interface TransportData extends java.io.Serializable {

	public Long getRequestId();
	
	public void setRequestId(Long id) ;
}
