/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: MessageHandler.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月15日 下午5:33:28
 * @version V1.0
 */

package com.alacoder.lion.remote;

/**
 * @ClassName: MessageHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月15日 下午5:33:28
 *
 */

public interface MessageHandler {

	 Object handle( Object message);
}
