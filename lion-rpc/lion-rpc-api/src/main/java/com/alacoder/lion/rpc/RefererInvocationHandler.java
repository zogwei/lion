/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: RefererInvocationHandler.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午11:55:35
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.ExceptionUtil;
import com.alacoder.lion.common.utils.ReflectUtil;
import com.alacoder.lion.common.utils.RequestIdGenerator;
import com.alacoder.lion.rpc.ha.Cluster;
import com.alacoder.lion.rpc.remote.DefaultRpcRequest;
import com.alacoder.lion.rpc.remote.RpcResponse;
import com.alacoder.lion.rpc.utils.LionFrameworkUtil;

/**
 * @ClassName: RefererInvocationHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午11:55:35
 *
 */

public class RefererInvocationHandler<T> implements InvocationHandler {

	private final static LogService logger = LogFactory.getLogService(RefererInvocationHandler.class);
	
	private List<Cluster<T>> clusters;
	private Class<T> clz;
	
    public RefererInvocationHandler(Class<T> clz, Cluster<T> cluster) {
    	 this.clusters = new ArrayList<Cluster<T>>(1);
         this.clusters.add(cluster);
         this.clz = clz;
    }
    
    public RefererInvocationHandler(Class<T> clz, List<Cluster<T>> clusters) {
        this.clz = clz;
        this.clusters = clusters;

        init();
    }

    private void init() {
        // clusters 不应该为空
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        DefaultRpcRequest request = new DefaultRpcRequest();

        request.setRequestId(RequestIdGenerator.getRequestId());
        request.setArguments(args);
        request.setMethodName(method.getName());
        request.setParamtersDesc(ReflectUtil.getMethodParamDesc(method));
        request.setInterfaceName(clz.getName());
        request.setAttachment(URLParamType.requestIdFromClient.getName(), String.valueOf(RequestIdGenerator.getRequestIdFromClient()));

        // 当 referer配置多个protocol的时候，比如A,B,C，
        // 那么正常情况下只会使用A，如果A被开关降级，那么就会使用B，B也被降级，那么会使用C
        for (Cluster<T> cluster : clusters) {
//            String protocolSwitcher = LionConstants.PROTOCOL_SWITCHER_PREFIX + cluster.getUrl().getProtocol();
//
//            Switcher switcher = switcherService.getSwitcher(protocolSwitcher);
//
//            if (switcher != null && !switcher.isOn()) {
//                continue;
//            }

            request.setAttachment(URLParamType.version.getName(), cluster.getUrl().getVersion());
            request.setAttachment(URLParamType.clientGroup.getName(), cluster.getUrl().getGroup());
            // 带上client的application和module
            request.setAttachment(URLParamType.application.getName(), ApplicationInfo.getApplication(cluster.getUrl()).getApplication());
            request.setAttachment(URLParamType.module.getName(), ApplicationInfo.getApplication(cluster.getUrl()).getModule());
            RpcResponse response = null;
            boolean throwException =
                    Boolean.parseBoolean(cluster.getUrl().getParameter(URLParamType.throwException.getName(),
                            URLParamType.throwException.getValue()));
            try {
                response = cluster.call(request);
                return response.getValue();
            } catch (RuntimeException e) {
                if (ExceptionUtil.isBizException(e)) {
                    Throwable t = e.getCause();
                    // 只抛出Exception，防止抛出远程的Error
                    if (t != null && t instanceof Exception) {
                        throw t;
                    } else {
                        String msg =
                                t == null ? "biz exception cause is null" : ("biz exception cause is throwable error:" + t.getClass()
                                        + ", errmsg:" + t.getMessage());
                        throw new LionServiceException(msg, LionErrorMsgConstant.SERVICE_DEFAULT_ERROR);
                    }
                } else if (!throwException) {
                    logger.warn("RefererInvocationHandler invoke false, so return default value: uri=" + cluster.getUrl().getUri()
                            + " " + LionFrameworkUtil.toString(request), e);
                    return getDefaultReturnValue(method.getReturnType());
                } else {
                    logger.error(
                            "RefererInvocationHandler invoke Error: uri=" + cluster.getUrl().getUri() + " "
                                    + LionFrameworkUtil.toString(request), e);
                    throw e;
                }
            }
        }

        throw new LionServiceException("Referer call Error: cluster not exist, interface=" + clz.getName() + " "
                + LionFrameworkUtil.toString(request), LionErrorMsgConstant.SERVICE_UNFOUND);

    }

    private Object getDefaultReturnValue(Class<?> returnType) {
        if (returnType != null && returnType.isPrimitive()) {
            return PrimitiveDefault.getDefaultReturnValue(returnType);
        }
        return null;
    }

    private static class PrimitiveDefault {
        private static boolean defaultBoolean;
        private static char defaultChar;
        private static byte defaultByte;
        private static short defaultShort;
        private static int defaultInt;
        private static long defaultLong;
        private static float defaultFloat;
        private static double defaultDouble;

        private static Map<Class<?>, Object> primitiveValues = new HashMap<Class<?>, Object>();

        static {
            primitiveValues.put(boolean.class, defaultBoolean);
            primitiveValues.put(char.class, defaultChar);
            primitiveValues.put(byte.class, defaultByte);
            primitiveValues.put(short.class, defaultShort);
            primitiveValues.put(int.class, defaultInt);
            primitiveValues.put(long.class, defaultLong);
            primitiveValues.put(float.class, defaultFloat);
            primitiveValues.put(double.class, defaultDouble);
        }

        public static Object getDefaultReturnValue(Class<?> returnType) {
            return primitiveValues.get(returnType);
        }

    }
}

