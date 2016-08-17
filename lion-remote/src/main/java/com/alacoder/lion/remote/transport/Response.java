/**
 * 版权声明：bee 版权所有 违者必究 2016
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

package com.alacoder.lion.remote.transport;

import java.util.Map;

import com.alacoder.lion.remote.TransportData;

/**
 * @ClassName: Response
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:08:53
 *
 */

public interface Response extends TransportData{

    /**
     * <pre>
	 * 		如果 request 正常处理，那么会返回 Object value，而如果 request 处理有异常，那么 getValue 会抛出异常
	 * </pre>
     * 
     * @throws RuntimeException
     * @return
     */
    Object getValue();

    /**
     * 如果request处理有异常，那么调用该方法return exception 如果request还没处理完或者request处理正常，那么return null
     * 
     * <pre>
	 * 		该方法不会阻塞，无论该request是处理中还是处理完成
	 * </pre>
     * 
     * @return
     */
    Exception getException();

    /**
     * 业务处理时间
     * 
     * @return
     */
    long getProcessTime();

    /**
     * 业务处理时间
     * 
     * @param time
     */
    void setProcessTime(long time);

    int getTimeout();

    Map<String, String> getAttachments();

    void setAttachment(String key, String value);

    // 获取rpc协议版本，可以依据协议版本做返回值兼容
    void setRpcProtocolVersion(byte rpcProtocolVersion);

    byte getRpcProtocolVersion();
}
