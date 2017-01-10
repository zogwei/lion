/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: ServiceConfig.java
 * @Package com.alacoder.lion.config
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午6:14:20
 * @version V1.0
 */

package com.alacoder.lion.rpc.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.ConcurrentHashSet;
import com.alacoder.lion.common.utils.NetUtils;
import com.alacoder.lion.common.utils.StringTools;
import com.alacoder.lion.registry.api.RegistryService;
import com.alacoder.lion.rpc.ConfigHandler;
import com.alacoder.lion.rpc.Exporter;

/**
 * @ClassName: ServiceConfig
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午6:14:20
 *
 */

public class ServiceConfig<T> extends AbstractServiceConfig {
	
	private final static LogService logger = LogFactory.getLogService(ServiceConfig.class);

	private static final long serialVersionUID = 1L;

	private AtomicBoolean exported = new AtomicBoolean(false);
    
	private T ref;
	private Class<T> interfaceClass;
	 // 具体到方法的配置
	private BasicServiceInterfaceConfig basicServiceConfig;
    // 具体到方法的配置
    protected List<MethodConfig> methods;
    
    // service的用于注册的url，用于管理service注册的生命周期，url为regitry url，内部嵌套service url。
    private ConcurrentHashSet<LionURL> registereUrls = new ConcurrentHashSet<LionURL>();
    // service 对应的exporters，用于管理service服务的生命周期
    private List<Exporter<T>> exporters = new CopyOnWriteArrayList<Exporter<T>>();
    
    private static ConcurrentHashSet<String> existingServices = new ConcurrentHashSet<String>();
    
    public synchronized void  export() {
    	 if (exported.get()) {
             logger.warn(String.format("%s has already been expoted, so ignore the export request!", interfaceClass.getName()));
             return;
         }
    	 
    	 checkInterfaceAndMethods(interfaceClass, methods);
    	 
    	 List<LionURL> registryUrls = loadRegistryUrls();
    	 if (registryUrls == null || registryUrls.size() == 0) {
             throw new IllegalStateException("Should set registry config for service:" + interfaceClass.getName());
         }
    	 //TODO 不支持一个协议的多个端口？如果多个协议同一个端口会怎么样？
    	 Map<String, Integer> protocolPorts = getProtocolAndPort();
         for (ProtocolConfig protocolConfig : protocols) {
             Integer port = protocolPorts.get(protocolConfig.getId());
             if (port == null) {
                 throw new LionServiceException(String.format("Unknow port in service:%s, protocol:%s", interfaceClass.getName(),
                         protocolConfig.getId()));
             }
             doExport(protocolConfig, port, registryUrls);
         }

         afterExport();
    	 
    }
    
    public synchronized void unexport() {
        if (!exported.get()) {
            return;
        }
        try {
            ConfigHandler configHandler =
                    ExtensionLoader.getExtensionLoader(ConfigHandler.class).getExtension(LionConstants.DEFAULT_VALUE);
            configHandler.unexport(exporters, registereUrls);
        } finally {
            afterUnexport();
        }
    }

    private void doExport(ProtocolConfig protocolConfig, int port, List<LionURL> registryURLs) {
        String protocolName = protocolConfig.getName();
        if (protocolName == null || protocolName.length() == 0) {
            protocolName = URLParamType.protocol.getValue();
        }

        String hostAddress = host;
        if (StringUtils.isBlank(hostAddress) && basicServiceConfig != null) {
            hostAddress = basicServiceConfig.getHost();
        }
        if (NetUtils.isInvalidLocalHost(hostAddress)) {
            hostAddress = getLocalHostAddress(registryURLs);
        }

        Map<String, String> map = new HashMap<String, String>();

        map.put(URLParamType.nodeType.getName(), LionConstants.NODE_TYPE_SERVICE);
        map.put(URLParamType.refreshTimestamp.getName(), String.valueOf(System.currentTimeMillis()));

        collectConfigParams(map, protocolConfig, basicServiceConfig, extConfig, this);
        collectMethodConfigParams(map, this.getMethods());

        LionURL serviceUrl = new LionURL(protocolName, hostAddress, port, interfaceClass.getName(), map);

        if (serviceExists(serviceUrl)) {
            logger.warn(String.format("%s configService is malformed, for same service (%s) already exists ", interfaceClass.getName(),
                    serviceUrl.getIdentity()));
            throw new LionFrameworkException(String.format("%s configService is malformed, for same service (%s) already exists ",
                    interfaceClass.getName(), serviceUrl.getIdentity()), LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
        }

        List<LionURL> urls = new ArrayList<LionURL>();

        // injvm 协议只支持注册到本地，其他协议可以注册到local、remote
        if (LionConstants.PROTOCOL_INJVM.equals(protocolConfig.getId())) {
        	LionURL localRegistryUrl = null;
            for (LionURL ru : registryURLs) {
                if (LionConstants.REGISTRY_PROTOCOL_LOCAL.equals(ru.getProtocol())) {
                    localRegistryUrl = ru.createCopy();
                    break;
                }
            }
            if (localRegistryUrl == null) {
                localRegistryUrl =
                        new LionURL(LionConstants.REGISTRY_PROTOCOL_LOCAL, hostAddress, LionConstants.DEFAULT_INT_VALUE,
                                RegistryService.class.getName());
            }

            urls.add(localRegistryUrl);
        } else {
            for (LionURL ru : registryURLs) {
                urls.add(ru.createCopy());
            }
        }

        for (LionURL u : urls) {
            u.addParameter(URLParamType.embed.getName(), StringTools.urlEncode(serviceUrl.toFullStr()));
            registereUrls.add(u.createCopy());
        }

        ConfigHandler configHandler = ExtensionLoader.getExtensionLoader(ConfigHandler.class).getExtension(LionConstants.DEFAULT_VALUE);

        exporters.add(configHandler.export(interfaceClass, ref, urls));

        initLocalAppInfo(serviceUrl);
    }

    private void afterExport() {
        exported.set(true);
        for (Exporter<T> ep : exporters) {
            existingServices.add(ep.getProvider().getUrl().getIdentity());
        }
    }

    private void afterUnexport() {
        exported.set(false);
        for (Exporter<T> ep : exporters) {
            existingServices.remove(ep.getProvider().getUrl().getIdentity());
            exporters.remove(ep);
        }
        exporters.clear();
        registereUrls.clear();
    }
    
    public Map<String, Integer> getProtocolAndPort() {
        if (StringUtils.isBlank(export)) {
            throw new LionServiceException("export should not empty in service config:" + interfaceClass.getName());
        }
        return ConfigUtil.parseExport(this.export);
    }
    
    protected boolean serviceExists(LionURL url) {
        return existingServices.contains(url.getIdentity());
    }

	public AtomicBoolean getExported() {
		return exported;
	}

	public void setExported(AtomicBoolean exported) {
		this.exported = exported;
	}

	public T getRef() {
		return ref;
	}

	public void setRef(T ref) {
		this.ref = ref;
	}

    public Class<?> getInterface() {
        return interfaceClass;
    }

    public void setInterface(Class<T> interfaceClass) {
        if (interfaceClass != null && !interfaceClass.isInterface()) {
            throw new IllegalStateException("The interface class " + interfaceClass + " is not a interface!");
        }
        this.interfaceClass = interfaceClass;
    }


	public BasicServiceInterfaceConfig getBasicServiceConfig() {
		return basicServiceConfig;
	}

	public void setBasicServiceConfig(BasicServiceInterfaceConfig basicServiceConfig) {
		this.basicServiceConfig = basicServiceConfig;
	}

	public List<MethodConfig> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodConfig> methods) {
		this.methods = methods;
	}

	public ConcurrentHashSet<LionURL> getRegistereUrls() {
		return registereUrls;
	}

	public void setRegistereUrls(ConcurrentHashSet<LionURL> registereUrls) {
		this.registereUrls = registereUrls;
	}

	public List<Exporter<T>> getExporters() {
		return exporters;
	}

	public void setExporters(List<Exporter<T>> exporters) {
		this.exporters = exporters;
	}

	public static ConcurrentHashSet<String> getExistingServices() {
		return existingServices;
	}

	public static void setExistingServices(
			ConcurrentHashSet<String> existingServices) {
		ServiceConfig.existingServices = existingServices;
	}
}
