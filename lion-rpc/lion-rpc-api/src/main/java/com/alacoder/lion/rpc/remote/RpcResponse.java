/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-api
 * @Title: DefaultResponse.java
 * @Package com.alacoder.lion.remote.transport
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月10日 下午5:48:11
 * @version V1.0
 */

package com.alacoder.lion.rpc.remote;

import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: DefaultResponse
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月10日 下午5:48:11
 *
 */

//public class RpcResponse extends DefaultResponse {
public abstract class RpcResponse<T> implements Response<T>{

}
