/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractExporter.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 下午2:04:40
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: AbstractExporter
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 下午2:04:40
 *
 */

public abstract class AbstractExporter<T> extends AbstractNode implements Exporter<T> {
	protected Provider<T> provider;

	public AbstractExporter(Provider<T> provider,LionURL url) {
		super(url);
		this.provider = provider;
	}

    public Provider<T> getProvider() {
        return provider;
    }

    @Override
    public String desc() {
        return "[" + this.getClass().getSimpleName() + "] url=" + url;
    }

}
