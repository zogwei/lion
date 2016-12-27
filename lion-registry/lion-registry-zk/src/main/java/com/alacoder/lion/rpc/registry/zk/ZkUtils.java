/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: ZkUtils.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月23日 下午7:14:02
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry.zk;

import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.url.LionURL;

/**
 * @ClassName: ZkUtils
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月23日 下午7:14:02
 *
 */


public class ZkUtils {

    public static String toGroupPath(LionURL url) {
//        return LionConstants.ZOOKEEPER_REGISTRY_NAMESPACE + LionConstants.PATH_SEPARATOR + url.getGroup();
    	return LionConstants.PATH_SEPARATOR + url.getGroup();
    }

    public static String toServicePath(LionURL url) {
        return toGroupPath(url) + LionConstants.PATH_SEPARATOR + url.getPath();
    }

    public static String toCommandPath(LionURL url) {
        return toGroupPath(url) + LionConstants.ZOOKEEPER_REGISTRY_COMMAND;
    }

    public static String toNodeTypePath(LionURL url, ZkNodeType nodeType) {
        String type;
        if (nodeType == ZkNodeType.AVAILABLE_SERVER) {
            type = "server";
        } else if (nodeType == ZkNodeType.UNAVAILABLE_SERVER) {
            type = "unavailableServer";
        } else if (nodeType == ZkNodeType.CLIENT) {
            type = "client";
        } else {
            throw new LionFrameworkException(String.format("Failed to get nodeTypePath, url: %s type: %s", url, nodeType.toString()));
        }
        return toServicePath(url) + LionConstants.PATH_SEPARATOR + type;
    }

    public static String toNodePath(LionURL url, ZkNodeType nodeType) {
        return toNodeTypePath(url, nodeType) + LionConstants.PATH_SEPARATOR + url.getServerPortStr();
    }
}
