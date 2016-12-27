/*
 *  Copyright 2009-2016 Weibo, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.alacoder.lion.rpc.springsupport;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;

import com.alacoder.lion.common.utils.CollectionUtil;
import com.alacoder.lion.config.BasicRefererInterfaceConfig;
import com.alacoder.lion.config.ProtocolConfig;
import com.alacoder.lion.config.RefererConfig;
import com.alacoder.lion.config.RegistryConfig;
import com.alacoder.lion.rpc.springsupport.namespace.handler.LionNamespaceHandler;
import com.alacoder.lion.rpc.utils.LionFrameworkUtil;

import java.util.Arrays;


public class RefererConfigBean<T> extends RefererConfig<T> implements FactoryBean<T>, BeanFactoryAware, InitializingBean, DisposableBean {

    private static final long serialVersionUID = 8381310907161365567L;

    private transient BeanFactory beanFactory;

    @Override
    public T getObject() throws Exception {
        return getRef();
    }

    @Override
    public Class<?> getObjectType() {
        return getInterface();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // basicConfig需要首先配置，因为其他可能会依赖于basicConfig的配置

        checkAndConfigBasicConfig();
        checkAndConfigProtocols();
        checkAndConfigRegistry();

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * 检查并配置basicConfig
     */
    private void checkAndConfigBasicConfig() {
        if (getBasicReferer() == null) {
            if (LionNamespaceHandler.basicRefererConfigDefineNames.size() == 0) {
                if (beanFactory instanceof ListableBeanFactory) {
                    ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
                    String[] basicRefererConfigNames = listableBeanFactory.getBeanNamesForType
                            (BasicRefererInterfaceConfig
                                    .class);
                    LionNamespaceHandler.basicRefererConfigDefineNames.addAll(Arrays.asList(basicRefererConfigNames));
                }
            }
            for (String name : LionNamespaceHandler.basicRefererConfigDefineNames) {
                BasicRefererInterfaceConfig biConfig = beanFactory.getBean(name, BasicRefererInterfaceConfig.class);
                if (biConfig == null) {
                    continue;
                }
                if (LionNamespaceHandler.basicRefererConfigDefineNames.size() == 1) {
                    setBasicReferer(biConfig);
                } else if (biConfig.isDefault() != null && biConfig.isDefault().booleanValue()) {
                    setBasicReferer(biConfig);
                }
            }
        }
    }

    /**
     * 检查是否已经装配protocols，否则按basicConfig--->default路径查找
     */
    private void checkAndConfigProtocols() {
        if (CollectionUtil.isEmpty(getProtocols()) && getBasicReferer() != null
                && !CollectionUtil.isEmpty(getBasicReferer().getProtocols())) {
            setProtocols(getBasicReferer().getProtocols());
        }
        if (CollectionUtil.isEmpty(getProtocols())) {
            for (String name : LionNamespaceHandler.protocolDefineNames) {
                ProtocolConfig pc = beanFactory.getBean(name, ProtocolConfig.class);
                if (pc == null) {
                    continue;
                }
                if (LionNamespaceHandler.protocolDefineNames.size() == 1) {
                    setProtocol(pc);
                } else if (pc.isDefault() != null && pc.isDefault().booleanValue()) {
                    setProtocol(pc);
                }
            }
        }
        if (CollectionUtil.isEmpty(getProtocols())) {
            setProtocol(LionFrameworkUtil.getDefaultProtocolConfig());
        }
    }

    /**
     * 检查并配置registry
     */
    public void checkAndConfigRegistry() {
        if (CollectionUtil.isEmpty(getRegistries()) && getBasicReferer() != null
                && !CollectionUtil.isEmpty(getBasicReferer().getRegistries())) {
            setRegistries(getBasicReferer().getRegistries());
        }
        if (CollectionUtil.isEmpty(getRegistries())) {
            for (String name : LionNamespaceHandler.registryDefineNames) {
                RegistryConfig rc = beanFactory.getBean(name, RegistryConfig.class);
                if (rc == null) {
                    continue;
                }
                if (LionNamespaceHandler.registryDefineNames.size() == 1) {
                    setRegistry(rc);
                } else if (rc.isDefault() != null && rc.isDefault().booleanValue()) {
                    setRegistry(rc);
                }
            }
        }
        if (CollectionUtil.isEmpty(getRegistries())) {
            setRegistry(LionFrameworkUtil.getDefaultRegistryConfig());
        }
    }

    public void checkAndConfigExtInfo() {

    }
}
