/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-admin-web
 * @Title: SystemServiceImpl.java
 * @Package com.alacoder.lion.admin.web.sys.service.impl
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月24日 下午4:04:28
 * @version V1.0
 */

package com.alacoder.lion.admin.web.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alacoder.lion.admin.web.sys.service.SystemService;

/**
 * @ClassName: SystemServiceImpl
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月24日 下午4:04:28
 *
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class SystemServiceImpl implements SystemService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void login() {
		logger.info("SystemServiceImpl run !");
	}

}
