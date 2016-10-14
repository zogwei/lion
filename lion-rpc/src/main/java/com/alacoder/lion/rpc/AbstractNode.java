/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractNode.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 下午1:58:28
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.utils.LoggerUtil;

/**
 * @ClassName: AbstractNode
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 下午1:58:28
 *
 */

public abstract class AbstractNode implements Node {

	protected LionURL url;
	
	protected volatile boolean init = false;
	protected volatile boolean available = false;
	
	public AbstractNode(LionURL url) {
		this.url = url;
	}
	
	@Override
	public synchronized void init() {
		if(init) {
			LoggerUtil.warn(this.getClass().getSimpleName() + " node already init: " + desc());
			return;
		}
		
		boolean result = doInit();
		if(!result) {
			 LoggerUtil.error(this.getClass().getSimpleName() + " node init Error: " + desc());
	            throw new LionFrameworkException(this.getClass().getSimpleName() + " node init Error: " + desc(),
	                    LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
		}
		else {
			LoggerUtil.info(this.getClass().getSimpleName() + " node init Success: " + desc());

            init = true;
            available = true;
		}
	}
	
	protected abstract boolean doInit();
	
    @Override
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LionURL getUrl() {
        return url;
    }
}
