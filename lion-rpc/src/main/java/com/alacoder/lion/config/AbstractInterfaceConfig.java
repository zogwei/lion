/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractInterfaceConfig.java
 * @Package com.alacoder.lion.config
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:10:34
 * @version V1.0
 */

package com.alacoder.lion.config;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.NetUtils;
import com.alacoder.lion.common.utils.ReflectUtil;
import com.alacoder.lion.common.utils.UrlUtils;
import com.alacoder.lion.rpc.ApplicationInfo;
import com.alacoder.lion.rpc.registry.RegistryService;

/**
 * @ClassName: AbstractInterfaceConfig
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:10:34
 *
 */

public abstract class AbstractInterfaceConfig extends AbstractConfig {

	private static final long serialVersionUID = 1L;

	// 暴露、使用的协议，暴露可以使用多种协议，但client只能用一种协议进行访问，原因是便于client的管理
    protected List<ProtocolConfig> protocols;

    // 注册中心的配置列表
    protected List<RegistryConfig> registries;

    // 扩展配置点
    protected ExtConfig extConfig;

    // 应用名称
    protected String application;

    // 模块名称
    protected String module;

    // 分组
    protected String group;

    // 服务版本
    protected String version;

    // 代理类型
    protected String proxy;

    // 过滤器
    protected String filter;

    // 最大并发调用
    protected Integer actives;

    // 是否异步
    protected Boolean async;

    // 服务接口的失败mock实现类名
    protected String mock;

    // 是否共享 channel
    protected Boolean shareChannel;

    // if throw exception when call failure，the default value is ture
    protected Boolean throwException;

    // 请求超时时间
    protected Integer requestTimeout;

    // 是否注册
    protected Boolean register;

    // 是否记录访问日志，true记录，false不记录
    protected String accessLog;

    // 是否进行check，如果为true，则在监测失败后抛异常
    protected String check;

    // 重试次数
    protected Integer retries;

    // 是否开启gzip压缩
    protected Boolean usegz;

    // 进行gzip压缩的最小阈值，usegz开启，且大于此值时才进行gzip压缩。单位Byte
    protected Integer mingzSize;

    protected String codec;
    
    protected void checkInterfaceAndMethods(Class<?> interfaceClass, List<MethodConfig> methods) {
        if (interfaceClass == null) {
            throw new IllegalStateException("interface not allow null!");
        }
        if (!interfaceClass.isInterface()) {
            throw new IllegalStateException("The interface class " + interfaceClass + " is not a interface!");
        }
        // 检查方法是否在接口中存在
        if (methods != null && !methods.isEmpty()) {
            for (MethodConfig methodBean : methods) {
                String methodName = methodBean.getName();
                if (methodName == null || methodName.length() == 0) {
                    throw new IllegalStateException("<lion:method> name attribute is required! Please check: <lion:service interface=\""
                            + interfaceClass.getName() + "\" ... ><lion:method name=\"\" ... /></<lion:referer>");
                }
                java.lang.reflect.Method hasMethod = null;
                for (java.lang.reflect.Method method : interfaceClass.getMethods()) {
                    if (method.getName().equals(methodName)) {
                        if (methodBean.getArgumentTypes() != null
                                && ReflectUtil.getMethodParamDesc(method).equals(methodBean.getArgumentTypes())) {
                            hasMethod = method;
                            break;
                        }
                        if (methodBean.getArgumentTypes() != null) {
                            continue;
                        }
                        if (hasMethod != null) {
                            throw new LionFrameworkException("The interface " + interfaceClass.getName() + " has more than one method "
                                    + methodName + " , must set argumentTypes attribute.", LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
                        }
                        hasMethod = method;
                    }
                }
                if (hasMethod == null) {
                    throw new LionFrameworkException("The interface " + interfaceClass.getName() + " not found method " + methodName,
                            LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
                }
                methodBean.setArgumentTypes(ReflectUtil.getMethodParamDesc(hasMethod));
            }
        }
    }
    
    protected List<LionURL> loadRegistryUrls() {
        List<LionURL> registryList = new ArrayList<LionURL>();
        if (registries != null && !registries.isEmpty()) {
            for (RegistryConfig config : registries) {
                String address = config.getAddress();
                if (StringUtils.isBlank(address)) {
                    address = NetUtils.LOCALHOST + ":" + LionConstants.DEFAULT_INT_VALUE;
                }
                Map<String, String> map = new HashMap<String, String>();
                config.appendConfigParams(map);

                map.put(URLParamType.application.getName(), getApplication());
                map.put(URLParamType.path.getName(), RegistryService.class.getName());
                map.put(URLParamType.refreshTimestamp.getName(), String.valueOf(System.currentTimeMillis()));

                // 设置默认的registry protocol，parse完protocol后，需要去掉该参数
                if (!map.containsKey(URLParamType.protocol.getName())) {
                    if (address.contains("://")) {
                        map.put(URLParamType.protocol.getName(), address.substring(0, address.indexOf("://")));
                    }
                    map.put(URLParamType.protocol.getName(), LionConstants.REGISTRY_PROTOCOL_LOCAL);
                }
                // address内部可能包含多个注册中心地址
                List<LionURL> urls = UrlUtils.parseURLs(address, map);
                if (urls != null && !urls.isEmpty()) {
                    for (LionURL url : urls) {
                        url.removeParameter(URLParamType.protocol.getName());
                        registryList.add(url);
                    }
                }
            }
        }
        return registryList;
    }
    
    protected String getLocalHostAddress(List<LionURL> registryURLs) {

        String localAddress = null;

        Map<String, Integer> regHostPorts = new HashMap<String, Integer>();
        for (LionURL ru : registryURLs) {
            if (StringUtils.isNotBlank(ru.getHost()) && ru.getPort() > 0) {
                regHostPorts.put(ru.getHost(), ru.getPort());
            }
        }

        InetAddress address = NetUtils.getLocalAddress(regHostPorts);
        if (address != null) {
            localAddress = address.getHostAddress();
        }

        if (NetUtils.isValidLocalHost(localAddress)) {
            return localAddress;
        }
        throw new LionServiceException("Please config local server hostname with intranet IP first!",
                LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
    }
    
    protected void initLocalAppInfo(LionURL localUrl) {
        ApplicationInfo.addService(localUrl);
    }

    public Integer getRetries() {
        return retries;
    }

    protected String localServiceAddress;

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAccessLog() {
        return accessLog;
    }

    public void setAccessLog(String accessLog) {
        this.accessLog = accessLog;
    }

    public List<RegistryConfig> getRegistries() {
        return registries;
    }

    public void setRegistries(List<RegistryConfig> registries) {
        this.registries = registries;
    }

    public ExtConfig getExtConfig() {
        return extConfig;
    }

    public void setExtConfig(ExtConfig extConfig) {
        this.extConfig = extConfig;
    }

    public void setRegistry(RegistryConfig registry) {
        this.registries = Collections.singletonList(registry);
    }

    public Integer getActives() {
        return actives;
    }

    public void setActives(Integer actives) {
        this.actives = actives;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public String getMock() {
        return mock;
    }

    public void setMock(String mock) {
        this.mock = mock;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    @Deprecated
    public void setCheck(Boolean check) {
        this.check = String.valueOf(check);
    }

    public Boolean getShareChannel() {
        return shareChannel;
    }

    public void setShareChannel(Boolean shareChannel) {
        this.shareChannel = shareChannel;
    }

    public List<ProtocolConfig> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<ProtocolConfig> protocols) {
        this.protocols = protocols;
    }

    public void setProtocol(ProtocolConfig protocol) {
        this.protocols = Collections.singletonList(protocol);
    }

    public Boolean getThrowException() {
        return throwException;
    }

    public void setThrowException(Boolean throwException) {
        this.throwException = throwException;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public boolean hasProtocol() {
        return this.protocols != null && !this.protocols.isEmpty();
    }

    public Boolean getRegister() {
        return register;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    public String getLocalServiceAddress() {
        return localServiceAddress;
    }

    public void setLocalServiceAddress(String localServiceAddress) {
        this.localServiceAddress = localServiceAddress;
    }

    public Boolean getUsegz() {
        return usegz;
    }

    public void setUsegz(Boolean usegz) {
        this.usegz = usegz;
    }

    public Integer getMingzSize() {
        return mingzSize;
    }

    public void setMingzSize(Integer mingzSize) {
        this.mingzSize = mingzSize;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }
	
    
	
}
