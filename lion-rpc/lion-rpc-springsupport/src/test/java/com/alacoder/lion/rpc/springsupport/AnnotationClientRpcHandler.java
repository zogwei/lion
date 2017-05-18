/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-springsupport
 * @Title: AnnotationClientRpcHandler.java
 * @Package com.alacoder.lion.rpc.springsupport
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月27日 下午7:47:44
 * @version V1.0
 */

package com.alacoder.lion.rpc.springsupport;

import org.springframework.stereotype.Component;

import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.rpc.springsupport.annotation.LionReferer;

/**
 * @ClassName: AnnotationClientRpcHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月27日 下午7:47:44
 *
 */
@Component
public class AnnotationClientRpcHandler {

	private final static Log logger = LogFactory.getLog(AnnotationClientRpcHandler.class);
	
	
    @LionReferer
    private DemoService lionDemoService;

    public void test() {
        for (int i = 0; i < 10; i++) {
            System.out.println(lionDemoService.hello("lion handler" + i));
            logger.info("lion handler" + i);
        }
    }
}
