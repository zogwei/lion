/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: TransportException.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月10日 下午2:56:57
 * @version V1.0
 */

package com.alacoder.lion.remote;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @ClassName: TransportException
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月10日 下午2:56:57
 *
 */

public class TransportException extends IOException {

    private static final long serialVersionUID = 7057762354907226994L;

    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;

    public TransportException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message) {
        super(message);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public TransportException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message, Throwable cause) {
        super(message, cause);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

}
