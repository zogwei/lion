/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-common
 * @Title: LionFrameworkUtil.java
 * @Package com.alacoder.lion.common.utils
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午11:48:46
 * @version V1.0
 */

package com.alacoder.lion.rpc.utils;

import org.apache.commons.lang3.StringUtils;

import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.config.ProtocolConfig;
import com.alacoder.lion.config.RegistryConfig;
import com.alacoder.lion.remote.transport.Request;

/**
 * @ClassName: LionFrameworkUtil
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午11:48:46
 *
 */

public class LionFrameworkUtil {
	  /**
     * 目前根据 group/interface/version 来唯一标示一个服务
     * 
     * @param request
     * @return
     */

    public static String getServiceKey(Request request) {
        String version = getVersionFromRequest(request);
        String group = getGroupFromRequest(request);

        return getServiceKey(group, request.getInterfaceName(), version);
    }

    public static String getGroupFromRequest(Request request) {
        return getValueFromRequest(request, URLParamType.group.name(), URLParamType.group.getValue());
    }

    public static String getVersionFromRequest(Request request) {
        return getValueFromRequest(request, URLParamType.version.name(), URLParamType.version.getValue());
    }

    public static String getValueFromRequest(Request request, String key, String defaultValue) {
        String value = defaultValue;
        if (request.getAttachments() != null && request.getAttachments().containsKey(key)) {
            value = request.getAttachments().get(key);
        }
        return value;
    }

    /**
     * 目前根据 group/interface/version 来唯一标示一个服务
     *
     * @param url
     * @return
     */
    public static String getServiceKey(LionURL url) {
        return getServiceKey(url.getGroup(), url.getPath(), url.getVersion());
    }

    /**
     * protocol key: protocol://host:port/group/interface/version
     * 
     * @param url
     * @return
     */
    public static String getProtocolKey(LionURL url) {
        return url.getProtocol() + LionConstants.PROTOCOL_SEPARATOR + url.getServerPortStr() + LionConstants.PATH_SEPARATOR
                + url.getGroup() + LionConstants.PATH_SEPARATOR + url.getPath() + LionConstants.PATH_SEPARATOR + url.getVersion();
    }

    /**
     * 输出请求的关键信息： requestId=** interface=** method=**(**)
     * 
     * @param request
     * @return
     */
    public static String toString(Request request) {
        return "requestId=" + request.getRequestId() + " interface=" + request.getInterfaceName() + " method=" + request.getMethodName()
                + "(" + request.getParamtersDesc() + ")";
    }

    /**
     * 根据Request得到 interface.method(paramDesc) 的 desc
     * 
     * <pre>
	 * 		比如：
	 * 			package com.weibo.api.motan;
	 * 
	 * 		 	interface A { public hello(int age); }
	 * 
	 * 			那么return "com.weibo.api.motan.A.hell(int)"
	 * </pre>
     * 
     * @param request
     * @return
     */
    public static String getFullMethodString(Request request) {
        return getGroupFromRequest(request) + "_" + request.getInterfaceName() + "." + request.getMethodName() + "("
                + request.getParamtersDesc() + ")";
    }


    /**
     * 判断url:source和url:target是否可以使用共享的service channel(port) 对外提供服务
     * 
     * <pre>
	 * 		1） protocol
	 * 		2） codec 
	 * 		3） serialize
	 * 		4） maxContentLength
	 * 		5） maxServerConnection
	 * 		6） maxWorkerThread
	 * 		7） workerQueueSize
	 * 		8） heartbeatFactory
	 * </pre>
     * 
     * @param source
     * @param target
     * @return
     */
    public static boolean checkIfCanShallServiceChannel(LionURL source, LionURL target) {
        if (!StringUtils.equals(source.getProtocol(), target.getProtocol())) {
            return false;
        }

        if (!StringUtils.equals(source.getParameter(URLParamType.codec.getName()), target.getParameter(URLParamType.codec.getName()))) {
            return false;
        }

        if (!StringUtils.equals(source.getParameter(URLParamType.serialize.getName()),
                target.getParameter(URLParamType.serialize.getName()))) {
            return false;
        }

        if (!StringUtils.equals(source.getParameter(URLParamType.maxContentLength.getName()),
                target.getParameter(URLParamType.maxContentLength.getName()))) {
            return false;
        }

        if (!StringUtils.equals(source.getParameter(URLParamType.maxServerConnection.getName()),
                target.getParameter(URLParamType.maxServerConnection.getName()))) {
            return false;
        }

        if (!StringUtils.equals(source.getParameter(URLParamType.maxWorkerThread.getName()),
                target.getParameter(URLParamType.maxWorkerThread.getName()))) {
            return false;
        }

        if (!StringUtils.equals(source.getParameter(URLParamType.workerQueueSize.getName()),
                target.getParameter(URLParamType.workerQueueSize.getName()))) {
            return false;
        }

        return StringUtils.equals(source.getParameter(URLParamType.heartbeatFactory.getName()),
                target.getParameter(URLParamType.heartbeatFactory.getName()));

    }

    /**
     * 判断url:source和url:target是否可以使用共享的client channel(port) 对外提供服务
     * 
     * <pre>
	 * 		1） protocol
	 * 		2） codec 
	 * 		3） serialize
	 * 		4） maxContentLength
	 * 		5） maxClientConnection
	 * 		6） heartbeatFactory
	 * </pre>
     * 
     * @param source
     * @param target
     * @return
     */
    public static boolean checkIfCanShallClientChannel(LionURL source, LionURL target) {
        if (!StringUtils.equals(source.getProtocol(), target.getProtocol())) {
            return false;
        }

        if (!StringUtils.equals(source.getParameter(URLParamType.codec.getName()), target.getParameter(URLParamType.codec.getName()))) {
            return false;
        }

        if (!StringUtils.equals(source.getParameter(URLParamType.serialize.getName()),
                target.getParameter(URLParamType.serialize.getName()))) {
            return false;
        }

        if (!StringUtils.equals(source.getParameter(URLParamType.maxContentLength.getName()),
                target.getParameter(URLParamType.maxContentLength.getName()))) {
            return false;
        }

        if (!StringUtils.equals(source.getParameter(URLParamType.maxClientConnection.getName()),
                target.getParameter(URLParamType.maxClientConnection.getName()))) {
            return false;
        }

        return StringUtils.equals(source.getParameter(URLParamType.heartbeatFactory.getName()),
                target.getParameter(URLParamType.heartbeatFactory.getName()));

    }

    /**
     * serviceKey: group/interface/version
     * 
     * @param group
     * @param interfaceName
     * @param version
     * @return
     */
    private static String getServiceKey(String group, String interfaceName, String version) {
        return group + LionConstants.PATH_SEPARATOR + interfaceName + LionConstants.PATH_SEPARATOR + version;
    }
    
    /**
     * 获取默认lion协议配置
     * @return lion协议配置
     */
    public static ProtocolConfig getDefaultProtocolConfig(){
        ProtocolConfig pc = new ProtocolConfig();
        pc.setId("lion");
        pc.setName("lion");
        return pc;
    }
    
    /**
     * 默认本地注册中心
     * @return local registry
     */
    public static RegistryConfig getDefaultRegistryConfig(){
        RegistryConfig local = new RegistryConfig();
        local.setRegProtocol("local");
        return local;
    }
    
    
}
