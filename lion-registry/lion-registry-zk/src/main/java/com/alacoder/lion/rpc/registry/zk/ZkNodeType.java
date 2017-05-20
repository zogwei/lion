/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: ZkNodeType.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月23日 下午7:15:33
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry.zk;

/**
 * @ClassName: ZkNodeType
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月23日 下午7:15:33
 *
 */

public enum ZkNodeType {
    AVAILABLE_SERVER,
    UNAVAILABLE_SERVER,
    CLIENT
}
