/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractHaStrategy.java
 * @Package com.alacoder.lion.rpc.ha
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月17日 下午3:53:38
 * @version V1.0
 */

package com.alacoder.lion.rpc.ha;

import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: AbstractHaStrategy
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月17日 下午3:53:38
 *
 */

public abstract class AbstractHaStrategy<T> implements HaStrategy<T> {

    protected LionURL url;

    @Override
    public void setUrl(LionURL url) {
        this.url = url;
    }
}
