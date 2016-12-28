/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: ZkRegistryTest.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月6日 下午7:18:04
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry.zk;

import java.util.List;

import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.registry.api.NotifyListener;

/**
 * @ClassName: ZkRegistryTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月6日 下午7:18:04
 *
 */

public class ZkRegistryTest {

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("----------register-----------");
		LionURL url = new LionURL(LionConstants.REGISTRY_PROTOCOL_DIRECT, "127.0.0.1", 4455, "com.alacoder.lion.rpc.DemoService");
		
		url.addParameter("address", "127.0.0.1:2181");
		ZkRegistry zkRegistry = new ZkRegistry(url);
		zkRegistry.register(url);
		zkRegistry.available(url);
		ZkRegistry subReg = zkRegistry;
		
		Thread.sleep(1000);
		System.out.println("----------subscribe-----------------");
		 NotifyListener listener = new  NotifyListener(){

			@Override
			public void notify(LionURL registryUrl, List<LionURL> urls) {
				System.out.println(">>registryUrl :" + registryUrl);
				for(LionURL u : urls){
					System.out.println(">> url  :" + u.toFullStr());
				}
			}
		 };
		zkRegistry.subscribe(url, listener);
		
		Thread.sleep(1000);
		System.out.println("----------register another----------");
	    url = new LionURL(LionConstants.REGISTRY_PROTOCOL_DIRECT, "127.0.0.1", 4466, "com.alacoder.lion.rpc.DemoService");
		
		url.addParameter("address", "127.0.0.1:2181");
		zkRegistry = new ZkRegistry(url);
		zkRegistry.register(url);
		zkRegistry.available(url);

		Thread.sleep(1000);
		System.out.println("----------unregister-----------");
		zkRegistry.unregister(url);
		
		Thread.sleep(1000);
		System.out.println("----------unsubscribe----------");
		url = new LionURL(LionConstants.REGISTRY_PROTOCOL_DIRECT, "127.0.0.1", 4455, "com.alacoder.lion.rpc.DemoService");
		
		url.addParameter("address", "127.0.0.1:2181");
		subReg.unsubscribe(url, listener);
		
		
		Thread.sleep(2*1000);
		System.out.println("----------register another----------");
	    url = new LionURL(LionConstants.REGISTRY_PROTOCOL_DIRECT, "127.0.0.1", 4477, "com.alacoder.lion.rpc.DemoService");
		
		url.addParameter("address", "127.0.0.1:2181");
		zkRegistry = new ZkRegistry(url);
		zkRegistry.register(url);
		zkRegistry.available(url);


		
		
	}
}
