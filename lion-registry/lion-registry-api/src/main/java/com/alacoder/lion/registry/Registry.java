/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: Registry.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午5:43:39
 * @version V1.0
 */

package com.alacoder.lion.registry;

import com.alacoder.lion.common.extension.Spi;
import com.alacoder.lion.common.extension.Scope;
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: Registry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午5:43:39
 *
 */

@Spi(scope = Scope.SINGLETON)
public interface Registry extends RegistryService, DiscoveryService {

    LionURL getUrl();
}
