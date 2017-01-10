/**
 * 版权声明：lion 版权所有 违者必究 2016
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
import com.alacoder.lion.rpc.config.ProtocolConfig;
import com.alacoder.lion.rpc.config.RegistryConfig;
import com.alacoder.lion.rpc.config.ServiceConfig;

/**
 * @ClassName: ServiceConfigTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 下午3:53:48
 *
 */

public class ServiceConfigTest {


	public static void main(String[] args) {
		ProtocolConfig pc = new ProtocolConfig();
		pc.setName("lion");
		pc.setId(pc.getName());
//		pc.setEndpointFactory("mockEndpoint");

		RegistryConfig rc = new RegistryConfig();
		rc.setId("directRc");
		rc.setName("directRc");
		rc.setRegProtocol("direct");
		rc.setAddress("127.0.0.1:6000");
		
        ServiceConfig<DemoService> serviceConfig = new ServiceConfig<DemoService>();
        serviceConfig.setApplication("demoApplication");
        serviceConfig.setModule("demoModule");
        serviceConfig.setGroup("demoGroup");
        serviceConfig.setCheck("true");
        serviceConfig.setInterface(DemoService.class);
        serviceConfig.setRef(new DemoServiceImple());
        serviceConfig.setShareChannel(true);
        
		serviceConfig.setProtocol(pc);
		serviceConfig.setRegistry(rc);
		serviceConfig.setExport("lion:7080");

		serviceConfig.export();
	}

}
