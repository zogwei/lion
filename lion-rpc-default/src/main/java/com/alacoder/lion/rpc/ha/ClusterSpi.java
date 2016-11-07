/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: ClusterSpi.java
 * @Package com.alacoder.lion.rpc.ha
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月10日 上午10:24:53
 * @version V1.0
 */

package com.alacoder.lion.rpc.ha;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alacoder.common.exception.LionAbstractException;
import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.CollectionUtil;
import com.alacoder.lion.common.utils.ExceptionUtil;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;
import com.alacoder.lion.rpc.Referer;

/**
 * @ClassName: ClusterSpi
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月10日 上午10:24:53
 *
 */
@SpiMeta(name = "default")
public class ClusterSpi<T> implements Cluster<T> {

    private HaStrategy<T> haStrategy;

    private LoadBalance<T> loadBalance;

    private List<Referer<T>> referers;

    private AtomicBoolean available = new AtomicBoolean(false);

    private LionURL url;

    @Override
    public void init() {
        onRefresh(referers);
        available.set(true);
    }

    @Override
    public Class<T> getInterface() {
        if (referers == null || referers.isEmpty()) {
            return null;
        }

        return referers.get(0).getInterface();
    }

    @Override
    public Response call(Request request) {
        if (available.get()) {
            try {
                return haStrategy.call(request, loadBalance);
            } catch (Exception e) {
                return callFalse(request, e);
            }
        }
        return callFalse(request, new LionServiceException(LionErrorMsgConstant.SERVICE_UNFOUND));
    }

    @Override
    public String desc() {
        return toString();
    }

    @Override
    public void destroy() {
        available.set(false);
        for (Referer<T> referer : this.referers) {
            referer.destroy();
        }
    }

    @Override
    public LionURL getUrl() {
        return url;
    }

    @Override
    public void setUrl(LionURL url) {
        this.url = url;
    }

    @Override
    public boolean isAvailable() {
        return available.get();
    }

    @Override
    public String toString() {
        return "cluster: {" + "ha=" + haStrategy + ",loadbalance=" + loadBalance +
                "referers=" + referers + "}";

    }

    @Override
    public synchronized void onRefresh(List<Referer<T>> referers) {
        if (CollectionUtil.isEmpty(referers)) {
            return;
        }

        loadBalance.onRefresh(referers);
        List<Referer<T>> oldReferers = this.referers;
        this.referers = referers;
        haStrategy.setUrl(getUrl());

        if (oldReferers == null || oldReferers.isEmpty()) {
            return;
        }

        List<Referer<T>> delayDestroyReferers = new ArrayList<Referer<T>>();

        for (Referer<T> referer : oldReferers) {
            if (referers.contains(referer)) {
                continue;
            }

            delayDestroyReferers.add(referer);
        }

        if (!delayDestroyReferers.isEmpty()) {
        	//TODO RefererSupports
//            RefererSupports.delayDestroy(delayDestroyReferers);
        }
    }

    public AtomicBoolean getAvailable() {
        return available;
    }

    public void setAvailable(AtomicBoolean available) {
        this.available = available;
    }

    public HaStrategy<T> getHaStrategy() {
        return haStrategy;
    }

    @Override
    public void setHaStrategy(HaStrategy<T> haStrategy) {
        this.haStrategy = haStrategy;
    }

    @Override
    public LoadBalance<T> getLoadBalance() {
        return loadBalance;
    }

    @Override
    public void setLoadBalance(LoadBalance<T> loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public List<Referer<T>> getReferers() {
        return referers;
    }

    protected Response callFalse(Request request, Exception cause) {

        // biz exception 无论如何都要抛出去
        if (ExceptionUtil.isBizException(cause)) {
            throw (RuntimeException) cause;
        }

        // 其他异常根据配置决定是否抛，如果抛异常，需要统一为LionException
        if (Boolean.parseBoolean(getUrl().getParameter(URLParamType.throwException.getName(), URLParamType.throwException.getValue()))) {
            if (cause instanceof LionAbstractException) {
                throw (LionAbstractException) cause;
            } else {
                LionServiceException lionException =
                        new LionServiceException(String.format("ClusterSpi Call false for request: %s", request), cause);
                throw lionException;
            }
        }

        return buildErrorResponse(request, cause);
    }

    private Response buildErrorResponse(Request request, Exception lionException) {
        DefaultResponse rs = new DefaultResponse();
        rs.setException(lionException);
        rs.setRequestId(request.getRequestId());
        return rs;
    }

}
