/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: NotifyListener.java
 * @Package com.alacoder.lion.rpc.registry
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午5:46:55
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry;

import java.util.List;

import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: NotifyListener
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午5:46:55
 *
 */

public interface NotifyListener {

    void notify(LionURL registryUrl, List<LionURL> urls);
}

