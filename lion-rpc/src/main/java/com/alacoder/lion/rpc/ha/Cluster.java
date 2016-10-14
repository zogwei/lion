/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: Cluster.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午11:51:52
 * @version V1.0
 */

package com.alacoder.lion.rpc.ha;

import java.util.List;

import com.alacoder.lion.common.extension.Spi;
import com.alacoder.lion.common.extension.Scope;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.rpc.Caller;
import com.alacoder.lion.rpc.Referer;
/**
 * @ClassName: Cluster
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午11:51:52
 *
 */

@Spi(scope = Scope.PROTOTYPE)
public interface Cluster<T> extends Caller<T> {

    @Override
    void init();

    void setUrl(LionURL url);

    void setLoadBalance(LoadBalance<T> loadBalance);

    void setHaStrategy(HaStrategy<T> haStrategy);

    void onRefresh(List<Referer<T>> referers);

    List<Referer<T>> getReferers();

    LoadBalance<T> getLoadBalance();

}
