/*
 *  Copyright 2009-2016 Weibo, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.alacoder.lion.rpc.springsupport;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoRpcClient {

    @SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) throws InterruptedException {
    	
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"classpath*:lion_demo_server.xml"});
        System.out.println("server start...");
        
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"classpath:lion_demo_client.xml"});

        DemoService service = (DemoService) ctx.getBean("lionDemoReferer");
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.println(service.hello("lion " + i));
            Thread.sleep(1000);
        }
        System.out.println("lion demo is finish.");
        System.exit(0);
    }

}
