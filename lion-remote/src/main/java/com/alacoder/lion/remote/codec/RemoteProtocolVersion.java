/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: RemoteProtocolVersion.java
 * @Package com.alacoder.lion.remote.codec
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月11日 上午10:13:50
 * @version V1.0
 */

package com.alacoder.lion.remote.codec;

/**
 * @ClassName: RemoteProtocolVersion
 * @Description: TODO
 * @author jimmy.zhong
 * @date 2016年8月11日 上午10:13:50
 *
 */

public enum RemoteProtocolVersion {

	VERSION_1((byte)1,16),VERSION_2((byte)2,16);
	
    private byte version;
    private int headerLength;

    RemoteProtocolVersion(byte version, int headerLength) {
        this.version = version;
        this.headerLength = headerLength;
    }

    public byte getVersion() {
        return version;
    }

    public int getHeaderLength() {
        return headerLength;
    }
}
