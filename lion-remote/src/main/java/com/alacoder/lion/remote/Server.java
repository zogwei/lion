/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: Server.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午2:54:42
 * @version V1.0
 */

package com.alacoder.lion.remote;

import java.util.Collection;

/**
 * @ClassName: Server
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午2:54:42
 *
 */

public interface Server extends Endpoint{
	

	Collection<Channel> getChannels();
	
	Channel getServerChannel();
}
