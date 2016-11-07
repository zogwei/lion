/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-springsupport
 * @Title: LionNamespaceHandler.java
 * @Package com.alacoder.lion.rpc.springsupport.namespace.handler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 下午7:10:59
 * @version V1.0
 */

package com.alacoder.lion.rpc.springsupport.namespace.handler;

import java.util.Set;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.alacoder.lion.common.utils.ConcurrentHashSet;
import com.alacoder.lion.config.BasicRefererInterfaceConfig;
import com.alacoder.lion.config.BasicServiceInterfaceConfig;
import com.alacoder.lion.config.ProtocolConfig;
import com.alacoder.lion.config.RegistryConfig;
import com.alacoder.lion.rpc.springsupport.AnnotationBean;
import com.alacoder.lion.rpc.springsupport.RefererConfigBean;
import com.alacoder.lion.rpc.springsupport.ServiceConfigBean;
import com.alacoder.lion.rpc.springsupport.SpiConfigBean;
import com.alacoder.lion.rpc.springsupport.namespace.parser.LionBeanDefinitionParser;

/**
 * @ClassName: LionNamespaceHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 下午7:10:59
 *
 */

public class LionNamespaceHandler extends NamespaceHandlerSupport {
    public final static Set<String> protocolDefineNames = new ConcurrentHashSet<String>();
    public final static Set<String> registryDefineNames = new ConcurrentHashSet<String>();
    public final static Set<String> basicServiceConfigDefineNames = new ConcurrentHashSet<String>();
    public final static Set<String> basicRefererConfigDefineNames = new ConcurrentHashSet<String>();

    @Override
    public void init() {
        registerBeanDefinitionParser("referer", new LionBeanDefinitionParser(RefererConfigBean.class, false));
        registerBeanDefinitionParser("service", new LionBeanDefinitionParser(ServiceConfigBean.class, true));
        registerBeanDefinitionParser("protocol", new LionBeanDefinitionParser(ProtocolConfig.class, true));
        registerBeanDefinitionParser("registry", new LionBeanDefinitionParser(RegistryConfig.class, true));
        registerBeanDefinitionParser("basicService", new LionBeanDefinitionParser(BasicServiceInterfaceConfig.class, true));
        registerBeanDefinitionParser("basicReferer", new LionBeanDefinitionParser(BasicRefererInterfaceConfig.class, true));
        registerBeanDefinitionParser("spi", new LionBeanDefinitionParser(SpiConfigBean.class, true));
        registerBeanDefinitionParser("annotation", new LionBeanDefinitionParser(AnnotationBean.class, true));

    }
}

