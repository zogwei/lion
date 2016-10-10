/**
 * 版权声明：bee 版权所有 违者必究 2016
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
import java.util.List;

import com.alacoder.lion.rpc.ha.Cluster;

/**
 * @ClassName: RefererInvocationHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月27日 上午11:55:35
 *
 */

public class RefererInvocationHandler<T> implements InvocationHandler {
	
    public RefererInvocationHandler(Class<T> clz, Cluster<T> cluster) {

    }
    
    public RefererInvocationHandler(Class<T> clz, List<Cluster<T>> clusters) {

    }

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
