/**
 * 版权声明：bee 版权所有 违者必究 2016
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
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: Filter
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午6:25:01
 *
 */

@Spi
public interface Filter {

    Response filter(Caller<?> caller, Request request);
}
