/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-default
 * @Title: FailoverHaStrategy.java
 * @Package com.alacoder.lion.rpc.ha
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月17日 下午3:54:52
 * @version V1.0
 */

package com.alacoder.lion.rpc.ha;

import java.util.ArrayList;
import java.util.List;

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.exception.LionServiceException;
import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.ExceptionUtil;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;
import com.alacoder.lion.rpc.Referer;
import com.alacoder.lion.rpc.remote.DefaultRpcRequest;
import com.alacoder.lion.rpc.remote.RpcRequestInfo;

/**
 * @ClassName: FailoverHaStrategy
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月17日 下午3:54:52
 *
 */

@SpiMeta(name = "failover")
public class FailoverHaStrategy<T> extends AbstractHaStrategy<T> {
	
	private final static Log logger = LogFactory.getLog(FailoverHaStrategy.class);

    protected ThreadLocal<List<Referer<T>>> referersHolder = new ThreadLocal<List<Referer<T>>>() {
        @Override
        protected java.util.List<Referer<T>> initialValue() {
            return new ArrayList<Referer<T>>();
        }
    };

    @SuppressWarnings("rawtypes")
	@Override
    public Response call(Request request, LoadBalance<T> loadBalance) {
    	DefaultRpcRequest rpcRequest = (DefaultRpcRequest)request;
    	RpcRequestInfo rpcRequestInfo =  rpcRequest.getRequestMsg();
    	
        List<Referer<T>> referers = selectReferers(request, loadBalance);
        if (referers.isEmpty()) {
            throw new LionServiceException(String.format("FailoverHaStrategy No referers for request:%s, loadbalance:%s", request,
                    loadBalance));
        }
        LionURL refUrl = referers.get(0).getUrl();
        // 先使用method的配置
        int tryCount =
                refUrl.getIdentityParameter(request.getIdentity(), URLParamType.retries.getName(),
                        URLParamType.retries.getIntValue());
        // 如果有问题，则设置为不重试
        if (tryCount < 0) {
            tryCount = 0;
        }

        for (int i = 0; i <= tryCount; i++) {
            Referer<T> refer = referers.get(i % referers.size());
            try {
            	rpcRequestInfo.setRetries(i);
                return refer.call(request);
            } catch (RuntimeException e) {
                // 对于业务异常，直接抛出
                if (ExceptionUtil.isBizException(e)) {
                    throw e;
                } else if (i >= tryCount) {
                    throw e;
                }
                logger.warn(String.format("FailoverHaStrategy Call false for request:%s error=%s", request, e.getMessage()));
            }
        }

        throw new LionFrameworkException("FailoverHaStrategy.call should not come here!");
    }

    @SuppressWarnings("rawtypes")
	protected List<Referer<T>> selectReferers(Request request, LoadBalance<T> loadBalance) {
        List<Referer<T>> referers = referersHolder.get();
        referers.clear();
        loadBalance.selectToHolder(request, referers);
        return referers;
    }
}