/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: DiscoveryService.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午5:45:52
 * @version V1.0
 */

package com.alacoder.lion.registry;

import java.util.List;

import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: DiscoveryService
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午5:45:52
 *
 */

public interface DiscoveryService {
    void subscribe(LionURL url, NotifyListener listener);

    void unsubscribe(LionURL url, NotifyListener listener);

    List<LionURL> discover(LionURL url);
    
}
