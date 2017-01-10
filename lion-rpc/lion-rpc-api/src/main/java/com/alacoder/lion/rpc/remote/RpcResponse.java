/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: Response.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:08:53
 * @version V1.0
 */

package com.alacoder.lion.rpc.remote;

import java.util.Map;

import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: Response
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:08:53
 *
 */

public interface RpcResponse extends Response{

    Map<String, String> getAttachments();

    void setAttachment(String key, String value);

    // 获取rpc协议版本，可以依据协议版本做返回值兼容
    void setRpcProtocolVersion(byte rpcProtocolVersion);

    byte getRpcProtocolVersion();
}
