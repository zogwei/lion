/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: MessageHandlerAdpter.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月9日 下午7:19:36
 * @version V1.0
 */

package com.alacoder.lion.remote;

import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: MessageHandlerAdpter
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月9日 下午7:19:36
 *
 */

public class MessageHandlerAdpter implements MessageHandler{


	@Override
	public Response<?> handle(Channel channel, Request<?> request) {
		return null;
	}

	@Override
	public Object handle(Channel channel, Response<?> response) {
		return false;
	}

	@Override
	public Object handle(Channel channel, TransportData response) {
		return null;
	}

}
