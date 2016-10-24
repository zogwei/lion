/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-default
 * @Title: ServiceConfigTest.java
 * @Package com.alacoder.lion.config
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 下午3:53:48
 * @version V1.0
 */

package com.alacoder.lion.config;

import com.alacoder.lion.rpc.DemoService;
import com.alacoder.lion.rpc.DemoServiceImple;

/**
 * @ClassName: ServiceConfigTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 下午3:53:48
 *
 */

public class ServiceConfigTest {


	public static void main(String[] args) {
        ServiceConfig<DemoService> serviceConfig = new ServiceConfig<DemoService>();
        serviceConfig.setRef(new DemoServiceImple());
        serviceConfig.setApplication("demoApplication");
        serviceConfig.setModule("demoModule");
        serviceConfig.setCheck("true");
        serviceConfig.setInterface(DemoService.class);
        serviceConfig.setGroup("demoGroup");
        serviceConfig.setShareChannel(true);
        
		ProtocolConfig pc = new ProtocolConfig();
		pc.setName("lion");
		pc.setId(pc.getName());
//		pc.setEndpointFactory("mockEndpoint");

		RegistryConfig rc = new RegistryConfig();
		rc.setRegProtocol("direct");
		rc.setName("direct");
		rc.setId(rc.getName());
		rc.setAddress("127.0.0.1:6000");
		
		serviceConfig.setProtocol(pc);
		serviceConfig.setRegistry(rc);
		serviceConfig.setExport("lion:7080");

		serviceConfig.export();
	}

}
