/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: Caller.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午4:32:35
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.rpc.remote.RpcRequest;
import com.alacoder.lion.rpc.remote.RpcResponse;

/**
 * @ClassName: Caller
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午4:32:35
 *
 */

public interface Caller<T> extends Node {

    Class<T> getInterface();

    RpcResponse call(RpcRequest request);
}
