/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: ZkRegistry.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月21日 下午7:14:44
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry.zk;

import java.util.List;

import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.rpc.registry.AbstractRegistry;
import com.alacoder.lion.rpc.registry.NotifyListener;

/**
 * @ClassName: ZkRegistry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月21日 下午7:14:44
 *
 */
@SpiMeta(name = "zk")
public class ZkRegistry extends AbstractRegistry {

    
	public ZkRegistry(LionURL url) {
		super(url);
	}

	@Override
	protected void doRegister(LionURL url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doUnregister(LionURL url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doSubscribe(LionURL url, NotifyListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doUnsubscribe(LionURL url, NotifyListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List<LionURL> doDiscover(LionURL url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doAvailable(LionURL url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doUnavailable(LionURL url) {
		// TODO Auto-generated method stub
		
	}

}
