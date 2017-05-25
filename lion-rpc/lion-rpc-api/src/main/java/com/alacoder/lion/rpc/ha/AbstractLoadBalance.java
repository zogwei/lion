/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractLoadBalance.java
 * @Package com.alacoder.lion.rpc.ha
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月14日 下午6:40:32
 * @version V1.0
 */

package com.alacoder.lion.rpc.ha;

import java.util.List;

import com.alacoder.common.exception.LionServiceException;
import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.rpc.Referer;

/**
 * @ClassName: AbstractLoadBalance
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月14日 下午6:40:32
 *
 */

public abstract class AbstractLoadBalance<T> implements LoadBalance<T> {

	private final static Log logger = LogFactory.getLog(AbstractLoadBalance.class);
	
	private List<Referer<T>> referers;

	@Override
	public void onRefresh(List<Referer<T>> referers) {
		 // 只能引用替换，不能进行referers update。
        this.referers = referers;
	}

	 @SuppressWarnings("rawtypes")
	@Override
	    public Referer<T> select(Request request) {
	        List<Referer<T>> referers = this.referers;

	        Referer<T> ref = null;
	        if (referers.size() > 1) {
	            ref = doSelect(request);

	        } else if (referers.size() == 1) {
	            ref = referers.get(0).isAvailable() ? referers.get(0) : null;
	        }

	        if (ref != null) {
	            return ref;
	        }
	        throw new LionServiceException(this.getClass().getSimpleName() + " No available referers for call request:" + request);
	    }


	@SuppressWarnings("rawtypes")
	@Override
	public void selectToHolder(Request request, List<Referer<T>> refersHolder) {
		 List<Referer<T>> referers = this.referers;

	        if (referers == null) {
	            throw new LionServiceException(this.getClass().getSimpleName() + " No available referers for call : referers_size= 0 "
	                    +" " + request);
	        }

	        if (referers.size() > 1) {
	            doSelectToHolder(request, refersHolder);

	        } else if (referers.size() == 1 && referers.get(0).isAvailable()) {
	            refersHolder.add(referers.get(0));
	        }
	        if (refersHolder.isEmpty()) {
	            throw new LionServiceException(this.getClass().getSimpleName() + " No available referers for call : referers_size="
	                    + referers.size() + " " + request);
	        }
	    }


    protected List<Referer<T>> getReferers() {
        return referers;
    }

    @Override
    public void setWeightString(String weightString) {
        logger.info("ignore weightString:" + weightString);
    }
	
    @SuppressWarnings("rawtypes")
	protected abstract Referer<T> doSelect(Request request);

    @SuppressWarnings("rawtypes")
	protected abstract void doSelectToHolder(Request request, List<Referer<T>> refersHolder);

}
