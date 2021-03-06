/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: DemoServiceImple.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午10:50:01
 * @version V1.0
 */

package com.alacoder.lion.rpc.springsupport;

import com.alacoder.lion.rpc.springsupport.annotation.LionService;

/**
 * @ClassName: DemoServiceImple
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午10:50:01
 *
 */
@LionService(export = "8002")
public class DemoServiceImple implements DemoService {

	@Override
	public String hello(String name) {
		
		return "hello, " + name;
	}

}
