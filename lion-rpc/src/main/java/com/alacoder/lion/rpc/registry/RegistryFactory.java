/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: RegistryFactory.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午5:44:33
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry;

import com.alacoder.lion.common.extension.Spi;
import com.alacoder.lion.common.extension.Scope;
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: RegistryFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午5:44:33
 *
 */

@Spi(scope = Scope.SINGLETON)
public interface RegistryFactory {

    Registry getRegistry(LionURL url);
}

