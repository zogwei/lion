/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: HaStrategy.java
 * @Package com.alacoder.lion.rpc.ha
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午3:11:31
 * @version V1.0
 */

package com.alacoder.lion.rpc.ha;

import com.alacoder.lion.common.extension.Spi;
import com.alacoder.lion.common.extension.Scope;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: HaStrategy
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午3:11:31
 *
 */

@Spi(scope = Scope.PROTOTYPE)
public interface HaStrategy<T> {

    void setUrl(LionURL url);

    Response call(Request request, LoadBalance<T> loadBalance);

}