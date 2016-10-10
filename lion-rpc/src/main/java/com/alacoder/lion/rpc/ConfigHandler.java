/**
 * 版权声明：bee 版权所有 违者必究 2016
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

import java.util.List;

import com.alacoder.lion.common.url.URL;
import com.alacoder.lion.rpc.ha.Cluster;


/**
 * @ClassName: ConfigHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 上午11:46:05
 *
 */

public interface ConfigHandler {

    <T> Exporter<T> export(Class<T> interfaceClass, T ref, List<URL> registryUrls);
    
    <T> T refer(Class<T> interfaceClass, List<Cluster<T>> cluster, String proxyType);
}
