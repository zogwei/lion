/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: ProtocolFilterDecorator.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午6:23:32
 * @version V1.0
 */

package com.alacoder.lion.rpc.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.extension.Activation;
import com.alacoder.lion.common.extension.ActivationComparator;
import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.rpc.Exporter;
import com.alacoder.lion.rpc.Filter;
import com.alacoder.lion.rpc.Protocol;
import com.alacoder.lion.rpc.Provider;
import com.alacoder.lion.rpc.Referer;
import com.alacoder.lion.rpc.remote.RpcRequest;
import com.alacoder.lion.rpc.remote.RpcResponse;

/**
 * @ClassName: ProtocolFilterDecorator
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午6:23:32
 *
 */


public class ProtocolFilterDecorator implements Protocol {

    private Protocol protocol;

    public ProtocolFilterDecorator(Protocol protocol) {
        if (protocol == null) {
            throw new LionFrameworkException("Protocol is null when construct ProtocolFilterDecorator",
                    LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
        }
        this.protocol = protocol;
    }

    @Override
    public <T> Exporter<T> export(Provider<T> provider, LionURL url) {
        return protocol.export(decorateWithFilter(provider, url), url);
    }

    @Override
    public <T> Referer<T> refer(Class<T> clz, LionURL url, LionURL serviceUrl) {
        return decorateWithFilter(protocol.refer(clz, url, serviceUrl), url);
    }

    public void destroy() {
        protocol.destroy();
    }

    private <T> Referer<T> decorateWithFilter(Referer<T> referer, LionURL url) {
        List<Filter> filters = getFilters(url, LionConstants.NODE_TYPE_REFERER);
        Referer<T> lastRef = referer;
        for (Filter filter : filters) {
            final Filter f = filter;
            final Referer<T> lf = lastRef;
            lastRef = new Referer<T>() {
                @Override
                public RpcResponse call(RpcRequest request) {
                    Activation activation = f.getClass().getAnnotation(Activation.class);
                    if (activation != null && !activation.retry() && request.getRetries() != 0) {
                        return lf.call(request);
                    }
                    return f.filter(lf, request);
                }

                @Override
                public String desc() {
                    return lf.desc();
                }

                @Override
                public void destroy() {
                    lf.destroy();
                }

                @Override
                public Class<T> getInterface() {
                    return lf.getInterface();
                }

                @Override
                public LionURL getUrl() {
                    return lf.getUrl();
                }

                @Override
                public void init() {
                    lf.init();
                }

                @Override
                public boolean isAvailable() {
                    return lf.isAvailable();
                }

                @Override
                public int activeRefererCount() {
                    return lf.activeRefererCount();
                }


                @Override
                public LionURL getServiceUrl() {
                    return lf.getServiceUrl();
                }
            };
        }
        return lastRef;
    }

    private <T> Provider<T> decorateWithFilter(Provider<T> provider, LionURL url) {
        List<Filter> filters = getFilters(url, LionConstants.NODE_TYPE_SERVICE);
        if (filters == null || filters.size() == 0) {
            return provider;
        }
        Provider<T> lastProvider = provider;
        for (Filter filter : filters) {
            final Filter f = filter;
            final Provider<T> lp = lastProvider;
            lastProvider = new Provider<T>() {
                @Override
                public RpcResponse call(RpcRequest request) {
                    return f.filter(lp, request);
                }

                @Override
                public String desc() {
                    return lp.desc();
                }

                @Override
                public void destroy() {
                    lp.destroy();
                }

                @Override
                public Class<T> getInterface() {
                    return lp.getInterface();
                }

                @Override
                public LionURL getUrl() {
                    return lp.getUrl();
                }

                @Override
                public void init() {
                    lp.init();
                }

                @Override
                public boolean isAvailable() {
                    return lp.isAvailable();
                }
            };
        }
        return lastProvider;
    }

    /**
     * <pre>
	 * 获取方式：
	 * 1）先获取默认的filter列表；
	 * 2）根据filter配置获取新的filters，并和默认的filter列表合并；
	 * 3）再根据一些其他配置判断是否需要增加其他filter，如根据accessLog进行判断，是否需要增加accesslog
	 * </pre>
     * 
     * @param url
     * @param key
     * @return
     */
    private List<Filter> getFilters(LionURL url, String key) {

        // load default filters
        List<Filter> filters = new ArrayList<Filter>();
        List<Filter> defaultFilters = ExtensionLoader.getExtensionLoader(Filter.class).getExtensions(key);
        if (defaultFilters != null && defaultFilters.size() > 0) {
            filters.addAll(defaultFilters);
        }

        // add filters via "filter" config
        String filterStr = url.getParameter(URLParamType.filter.getName());
        if (StringUtils.isNotBlank(filterStr)) {
            String[] filterNames = LionConstants.COMMA_SPLIT_PATTERN.split(filterStr);
            for (String fn : filterNames) {
                addIfAbsent(filters, fn);
            }
        }

        // add filter via other configs, like accessLog and so on
        boolean accessLog = url.getBooleanParameter(URLParamType.accessLog.getName(), URLParamType.accessLog.getBooleanValue());
        if (accessLog) {
            addIfAbsent(filters, AccessLogFilter.class.getAnnotation(SpiMeta.class).name());
        }

        // sort the filters
        Collections.sort(filters, new ActivationComparator<Filter>());
        Collections.reverse(filters);
        return filters;
    }

    private void addIfAbsent(List<Filter> filters, String extensionName) {
        if (StringUtils.isBlank(extensionName)) {
            return;
        }

        Filter extFilter = ExtensionLoader.getExtensionLoader(Filter.class).getExtension(extensionName);
        if (extFilter == null) {
            return;
        }

        boolean exists = false;
        for (Filter f : filters) {
            if (f.getClass() == extFilter.getClass()) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            filters.add(extFilter);
        }

    }
}