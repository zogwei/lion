/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: ProxyFactory.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午11:54:13
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.lang.reflect.InvocationHandler;

import com.alacoder.lion.common.extension.Spi;

/**
 * @ClassName: ProxyFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午11:54:13
 *
 */

@Spi
public interface ProxyFactory {

    <T> T getProxy(Class<T> clz, InvocationHandler invocationHandler);

}
