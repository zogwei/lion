/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-default
 * @Title: JdkProxyFactory.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午11:59:08
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.alacoder.lion.common.extension.SpiMeta;

/**
 * @ClassName: JdkProxyFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午11:59:08
 *
 */

@SpiMeta(name = "jdk")
public class JdkProxyFactory implements ProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clz, InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {clz}, invocationHandler);
    }

}
