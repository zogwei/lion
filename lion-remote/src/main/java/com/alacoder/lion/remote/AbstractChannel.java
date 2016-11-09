/**
 * 版权声明：lion 版权所有 违者必究 2016
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

import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: AbstractChannel
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月15日 下午4:08:27
 *
 */

public abstract class AbstractChannel implements Channel {

	protected LionURL url;
	

	
	public AbstractChannel(LionURL url) {
		this.url = url;

	}
	

	
}
