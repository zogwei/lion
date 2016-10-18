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

import java.util.HashMap;
import java.util.Map;

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
	
    // 多个service可能在相同端口进行服务暴露，因此来自同个端口的请求需要进行路由以找到相应的服务，同时不在该端口暴露的服务不应该被找到
    private Map<String, DefaultMessageHandler> ipPort2RequestRouter = new HashMap<String, DefaultMessageHandler>();

	@Override
	protected <T> Exporter<T> createExporter(Provider<T> provider, LionURL url) {
		 return new DefaultRpcExporter<T>(provider, url,ipPort2RequestRouter);
	}
	
    @Override
    protected <T> Referer<T> createReferer(Class<T> clz, LionURL url, LionURL serviceUrl) {
        return new DefaultRpcReferer<T>(clz, url, serviceUrl);
    }


}
