/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-springsupport
 * @Title: AnnotationRpcClientDemo.java
 * @Package com.alacoder.lion.rpc.springsupport
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月27日 下午7:49:35
 * @version V1.0
 */

package com.alacoder.lion.rpc.springsupport;

/**
 * @ClassName: AnnotationRpcClientDemo
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月27日 下午7:49:35
 *
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Configuration
public class AnnotationRpcClientDemo {

    public static void main(String[] args) throws InterruptedException {

        @SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new
                String[]{"classpath:lion_demo_client_annotation.xml"});


        AnnotationClientRpcHandler handler = (AnnotationClientRpcHandler) ctx.getBean("annotationClientRpcHandler");
        handler.test();


        System.out.println("lion demo is finish.");
        System.exit(0);
    }

//    /**
//     * 设置Bean是必须的
//     *
//     * @return
//     */
////    @Bean
//    public AnnotationBean lionAnnotationBean() {
//        AnnotationBean lionAnnotationBean = new AnnotationBean("com.weibo");
////        lionAnnotationBean.setPackageName("com.weibo");
//        return lionAnnotationBean;
//    }

    @Bean(name = "demoLion")
    public ProtocolConfigBean demoLionProtocolConfig() {
        ProtocolConfigBean config = new ProtocolConfigBean();
        //Id无需设置
//        config.setId("demoLion");
        config.setName("lion");
        config.setDefault(true);
        config.setMaxContentLength(1048576);
        config.setHaStrategy("failover");
//        config.setLoadbalance("roundrobin");
        return config;
    }

//    @Bean(name = "demoLion2")
//    public ProtocolConfigBean protocolConfig2() {
//        ProtocolConfigBean config = new ProtocolConfigBean();
//        config.setName("lion");
//        config.setMaxContentLength(1048576);
//        config.setHaStrategy("failover");
////        config.setLoadbalance("roundrobin");
//        return config;
//    }

    @Bean(name = "registry")
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
        config.setRegistry("registry");
        
        config.setGroup("lion-demo-rpc");
        config.setModule("lion-demo-rpc");
        config.setApplication("myLionDemo");
        
        config.setAccessLog(false);
        config.setShareChannel(true);

        return config;
    }


    @Bean(name = "liontestClientBasicConfig")
    public BasicRefererConfigBean baseRefererConfig() {
        BasicRefererConfigBean config = new BasicRefererConfigBean();
        config.setProtocol("demoLion");
        config.setRegistry("registry");
        
        config.setGroup("lion-demo-rpc");
        config.setModule("lion-demo-rpc");
        config.setApplication("myLionDemo");
        
        config.setCheck(false);
        config.setAccessLog(true);
        config.setRetries(2);
        config.setThrowException(true);
        return config;
    }



}
