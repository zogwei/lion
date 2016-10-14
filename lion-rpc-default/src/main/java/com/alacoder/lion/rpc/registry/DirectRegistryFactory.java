/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: DirectRegistryFactory.java
 * @Package com.alacoder.lion.rpc.registry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午6:51:25
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry;

import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: DirectRegistryFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午6:51:25
 *
 */
@SpiMeta(name = "direct")
public class DirectRegistryFactory extends AbstractRegistryFactory{

	@Override
	protected Registry createRegistry(LionURL url) {
		return new DirectRegistry(url);
	}

}
