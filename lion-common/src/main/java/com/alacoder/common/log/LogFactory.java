/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-common
 * @Title: LogFactory.java
 * @Package com.alacoder.common.log
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月27日 下午2:43:01
 * @version V1.0
 */

package com.alacoder.common.log;

/**
 * @ClassName: LogFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月27日 下午2:43:01
 *
 */

public class LogFactory {

	public static LogService getLogService(Class<?> clazz){
		com.aben.cup.log.logging.Log log = com.aben.cup.log.logging.LogFactory.getLog(clazz);
		return new DefaultLogService(log);
	}
}
