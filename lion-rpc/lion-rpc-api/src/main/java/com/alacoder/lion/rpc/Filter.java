/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: Filter.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午6:25:01
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.common.extension.Spi;
import com.alacoder.lion.rpc.remote.RpcRequest;
import com.alacoder.lion.rpc.remote.RpcResponse;

/**
 * @ClassName: Filter
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午6:25:01
 *
 */

@Spi
public interface Filter {

    RpcResponse filter(Caller<?> caller, RpcRequest request);
}
