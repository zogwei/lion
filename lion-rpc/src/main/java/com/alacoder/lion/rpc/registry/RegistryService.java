/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: RegistryService.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午5:44:03
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry;

import java.util.Collection;

import com.alacoder.lion.common.url.URL;

/**
 * @ClassName: RegistryService
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 下午5:44:03
 *
 */

public interface RegistryService {
    /**
     * register service to registry
     *
     * @param url
     */
    void register(URL url);

    /**
     * unregister service to registry
     *
     * @param url
     */
    void unregister(URL url);

    /**
     * set service status to available, so clients could use it
     *
     * @param url service url to be available, <b>null</b> means all services
     */
    void available(URL url);

    /**
     * set service status to unavailable, client should not discover services of unavailable state
     *
     * @param url service url to be unavailable, <b>null</b> means all services
     */
    void unavailable(URL url);

    Collection<URL> getRegisteredServiceUrls();
}
