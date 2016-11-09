/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-springsupport
 * @Title: AnnotationRpcServerDemo.java
 * @Package com.alacoder.lion.rpc.springsupport
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月27日 下午5:57:33
 * @version V1.0
 */

package com.alacoder.lion.rpc.springsupport;

/**
 * @ClassName: AnnotationRpcServerDemo
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月27日 下午5:57:33
 *
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Configuration
@ComponentScan
public class AnnotationRpcServerDemo {

    @SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new
                String[]{"classpath*:lion_demo_server_annotation.xml"});
//        LionSwitcherUtil.setSwitcherValue(LionConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
        System.out.println("server start...");

    }


    @Bean(name="demoLion")
    public ProtocolConfigBean protocolConfig1() {
        ProtocolConfigBean config = new ProtocolConfigBean();
        config.setDefault(true);
        config.setName("lion");
        config.setMaxContentLength(1048576);
        return config;
    }

    @Bean(name = "lion")
    public ProtocolConfigBean protocolConfig2() {
        ProtocolConfigBean config = new ProtocolConfigBean();
//        config.setId("demoLion2");
        config.setName("lion");
        config.setMaxContentLength(1048576);
        return config;
    }

    @Bean(name="registryConfig1")
    public RegistryConfigBean registryConfig() {
        RegistryConfigBean config = new RegistryConfigBean();
        config.setRegProtocol("direct");
//        config.setAddress("127.0.0.1:2181");
        return config;
    }


    @Bean
    public BasicServiceConfigBean baseServiceConfig() {
        BasicServiceConfigBean config = new BasicServiceConfigBean();
        config.setExport("demoLion:8002");
        config.setGroup("lion-demo-rpc");
        config.setAccessLog(false);
        config.setShareChannel(true);
        config.setModule("lion-demo-rpc");
        config.setApplication("myLionDemo");
        config.setRegistry("registryConfig1");
        return config;
    }

}
