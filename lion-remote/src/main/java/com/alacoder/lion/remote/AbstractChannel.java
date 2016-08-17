/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: AbstractChannel.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月15日 下午4:08:27
 * @version V1.0
 */

package com.alacoder.lion.remote;

import com.alacoder.lion.common.url.URL;

/**
 * @ClassName: AbstractChannel
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月15日 下午4:08:27
 *
 */

public abstract class AbstractChannel implements Channel {

	private URL url;
	
	private MessageHandler handler;
	
	public AbstractChannel(URL url,MessageHandler handler){
		this.url = url;
		this.handler = handler;
	}
}
