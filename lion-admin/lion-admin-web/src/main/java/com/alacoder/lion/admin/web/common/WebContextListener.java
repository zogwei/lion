/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-admin-web
 * @Title: WebContextListener.java
 * @Package com.alacoder.lion.admin.web.common
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月24日 下午3:26:15
 * @version V1.0
 */

package com.alacoder.lion.admin.web.common;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;

/**
 * @ClassName: WebContextListener
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月24日 下午3:26:15
 *
 */

public class WebContextListener extends org.springframework.web.context.ContextLoaderListener {
	
	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		return super.initWebApplicationContext(servletContext);
	}
}
