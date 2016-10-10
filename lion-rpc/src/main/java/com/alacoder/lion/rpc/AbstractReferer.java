/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractReferer.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月10日 下午2:40:17
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.util.concurrent.atomic.AtomicInteger;

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.url.URL;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;
import com.alacoder.lion.rpc.utils.LionFrameworkUtil;

/**
 * @ClassName: AbstractReferer
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月10日 下午2:40:17
 *
 */

public abstract class AbstractReferer<T> extends AbstractNode implements Referer<T> {
	
	protected Class<T> clz;
	protected AtomicInteger activeRefererCount = new AtomicInteger(0);
	protected URL serviceUrl;
	
	public AbstractReferer(Class<T> clz, URL url) {
		super(url);
		this.clz = clz;
		this.serviceUrl = url;
	}

	public AbstractReferer(Class<T> clz, URL url, URL serviceUrl) {
        super(url);
        this.clz = clz;
        this.serviceUrl = serviceUrl;
    }
	
    @Override
    public Class<T> getInterface() {
        return clz;
    }
    
    @Override
    public Response call(Request request) {
        if (!isAvailable()) {
            throw new LionFrameworkException(this.getClass().getSimpleName() + " call Error: node is not available, url=" + url.getUri()
                    + " " + LionFrameworkUtil.toString(request));
        }

        incrActiveCount(request);
        Response response = null;
        try {
            response = doCall(request);

            return response;
        } finally {
            decrActiveCount(request, response);
        }
    }

    @Override
    public int activeRefererCount() {
        return activeRefererCount.get();
    }

    protected void incrActiveCount(Request request) {
        activeRefererCount.incrementAndGet();
    }

    protected void decrActiveCount(Request request, Response response) {
        activeRefererCount.decrementAndGet();
    }

    protected abstract Response doCall(Request request);

    @Override
    public String desc() {
        return "[" + this.getClass().getSimpleName() + "] url=" + url;
    }

    @Override
    public URL getServiceUrl() {
        return serviceUrl;
    }
}
