/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-default
 * @Title: DefaultConfigHandler.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 上午11:47:12
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.util.Collection;
import java.util.List;

import com.alacoder.lion.registry.Registry;
import com.alacoder.lion.registry.RegistryFactory;

import com.alacoder.common.exception.LionErrorMsg;
import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.StringTools;
import com.alacoder.lion.rpc.ha.Cluster;
import com.alacoder.lion.rpc.ha.ClusterSupport;
import com.alacoder.lion.rpc.RefererInvocationHandler;
import com.alacoder.lion.common.LionConstants;
/**
 * @ClassName: DefaultConfigHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月26日 上午11:47:12
 *
 */
@SpiMeta(name = LionConstants.DEFAULT_VALUE)
public class DefaultConfigHandler implements ConfigHandler {
	
	private final static LogService logger = LogFactory.getLogService(DefaultConfigHandler.class);

    @Override
    public <T> ClusterSupport<T> buildClusterSupport(Class<T> interfaceClass, List<LionURL> registryUrls) {
        ClusterSupport<T> clusterSupport = new ClusterSupport<T>(interfaceClass, registryUrls);
        clusterSupport.init();

        return clusterSupport;
    }

    @Override
    public <T> Exporter<T> export(Class<T> interfaceClass, T ref, List<LionURL> registryUrls) {

        String serviceStr = StringTools.urlDecode(registryUrls.get(0).getParameter(URLParamType.embed.getName()));
        LionURL serviceUrl = LionURL.valueOf(serviceStr);

        // export service
        // 利用protocol decorator来增加filter特性
        String protocolName = serviceUrl.getParameter(URLParamType.protocol.getName(), URLParamType.protocol.getValue());
        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(protocolName);
        Provider<T> provider = new DefaultProvider<T>(ref, serviceUrl, interfaceClass);
        Exporter<T> exporter = protocol.export(provider, serviceUrl);

        // register service
        register(registryUrls, serviceUrl);

        return exporter;
    }
    
    private void register(List<LionURL> registryUrls, LionURL serviceUrl) {

        for ( LionURL url : registryUrls) {
            // 根据check参数的设置，register失败可能会抛异常，上层应该知晓
            RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(url.getProtocol());
            if (registryFactory == null) {
                throw new LionFrameworkException(new LionErrorMsg(500, LionErrorMsgConstant.FRAMEWORK_REGISTER_ERROR_CODE,
                        "register error! Could not find extension for registry protocol:" + url.getProtocol()
                                + ", make sure registry module for " + url.getProtocol() + " is in classpath!"));
            }
            Registry registry = registryFactory.getRegistry(url);
            registry.register(serviceUrl);
            registry.available(serviceUrl);
        }
    }

    @Override
    public<T> T refer(Class<T> interfaceClass, List<Cluster<T>> clusters, String proxyType) {
        ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(proxyType);
        return proxyFactory.getProxy(interfaceClass, new RefererInvocationHandler<T>(interfaceClass, clusters));
    }

    @Override
    public <T> void unexport(List<Exporter<T>> exporters, Collection<LionURL> registryUrls) {
        try {
            unRegister(registryUrls);
        } catch (Exception e1) {
            logger.warn("Exception when unregister urls:" + registryUrls);
        }
        try {
            for (Exporter<T> exporter : exporters) {
                exporter.unexport();
            }
        } catch (Exception e) {
            logger.warn("Exception when unexport exporters:" + exporters);
        }
    }
    
    private void unRegister(Collection<LionURL> registryUrls) {
        for (LionURL url : registryUrls) {
            // 不管check的设置如何，做完所有unregistry，做好清理工作
            try {
                String serviceStr = StringTools.urlDecode(url.getParameter(URLParamType.embed.getName()));
                LionURL serviceUrl = LionURL.valueOf(serviceStr);

                RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(url.getProtocol());
                Registry registry = registryFactory.getRegistry(url);
                registry.unregister(serviceUrl);
            } catch (Exception e) {
                logger.warn(String.format("unregister url false:%s", url), e);
            }
        }
    }

}
