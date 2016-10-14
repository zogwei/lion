/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: DefaultRpcProtocol.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 下午1:53:36
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: DefaultRpcProtocol
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 下午1:53:36
 *
 */
@SpiMeta(name = "lion")
public class DefaultRpcProtocol extends AbstractProtocol {

	@Override
	protected <T> Exporter<T> createExporter(Provider<T> provider, LionURL url) {
		 return new DefaultRpcExporter<T>(provider, url);
	}
	
    @Override
    protected <T> Referer<T> createReferer(Class<T> clz, LionURL url, LionURL serviceUrl) {
        return new DefaultRpcReferer<T>(clz, url, serviceUrl);
    }


}
