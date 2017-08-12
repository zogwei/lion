/*
 * Copyright 2009-2016 alacoder, Inc.
 */

package com.alacoder.lion.tcc.spring;

import com.alacoder.lion.tcc.spring.demo.DemoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TccTestMain {

    @SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) throws InterruptedException {
    	
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                                                                                   new String[] { "classpath*:spring-*.xml" });
        System.out.println("server start...");
        
        DemoService demoService = (DemoService) applicationContext.getBean("demoService");
        demoService.addData(System.currentTimeMillis());

        System.out.println("main demo is finish.");

        System.exit(0);
    }

}
