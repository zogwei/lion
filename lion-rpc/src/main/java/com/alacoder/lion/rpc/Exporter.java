/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: Exporter.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午11:11:54
 * @version V1.0
 */

package com.alacoder.lion.rpc;

/**
 * @ClassName: Exporter
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午11:11:54
 *
 */

public interface Exporter<T> extends Node {

    Provider<T> getProvider();

    void unexport();
}
