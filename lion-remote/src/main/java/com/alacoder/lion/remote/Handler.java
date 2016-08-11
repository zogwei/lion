/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: Handler.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午2:55:11
 * @version V1.0
 */

package com.alacoder.lion.remote;

/**
 * @ClassName: Handler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午2:55:11
 *
 */

public interface Handler {

	void connect() ;
	
	void messageRecevice();
	
	void close();
	
	void exception();
}
