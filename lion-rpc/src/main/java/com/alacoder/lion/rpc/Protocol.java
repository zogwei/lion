/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: Protocol.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午11:13:23
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.common.extension.Scope;
import com.alacoder.lion.common.extension.Spi;
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: Protocol
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午11:13:23
 *
 */
@Spi(scope = Scope.SINGLETON)
public interface Protocol {

    <T> Exporter<T> export(Provider<T> provider, LionURL url);
    
    <T> Referer<T> refer(Class<T> clz, LionURL url, LionURL serviceUrl);  
    
    void destroy();
    
}
