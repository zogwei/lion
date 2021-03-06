/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: ConfigHandler.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 上午11:46:05
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.util.Collection;
import java.util.List;

import com.alacoder.lion.common.extension.Spi;
import com.alacoder.lion.common.extension.Scope;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.rpc.ha.Cluster;
import com.alacoder.lion.rpc.ha.ClusterSupport;


/**
 * @ClassName: ConfigHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 上午11:46:05
 *
 */
@Spi(scope = Scope.SINGLETON)
public interface ConfigHandler {
	
    <T> ClusterSupport<T> buildClusterSupport(Class<T> interfaceClass, List<LionURL> registryUrls);

    <T> Exporter<T> export(Class<T> interfaceClass, T ref, List<LionURL> registryUrls);
    
    <T> T refer(Class<T> interfaceClass, List<Cluster<T>> cluster, String proxyType);
    
    <T> void unexport(List<Exporter<T>> exporters, Collection<LionURL> registryUrls);
}
