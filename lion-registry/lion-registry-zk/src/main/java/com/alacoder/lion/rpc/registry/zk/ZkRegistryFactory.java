/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: ZkRegistryFactory.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月21日 下午7:16:14
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry.zk;

import com.alacoder.lion.registry.AbstractRegistryFactory;
import com.alacoder.lion.registry.Registry;

import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: ZkRegistryFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月21日 下午7:16:14
 *
 */
@SpiMeta(name = "zookeeper")
public class ZkRegistryFactory extends AbstractRegistryFactory {

	@Override
	protected Registry createRegistry(LionURL url) {
		return new ZkRegistry(url);
	}

}
