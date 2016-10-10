/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: Node.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午4:32:00
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.common.url.URL;

/**
 * @ClassName: Node
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午4:32:00
 *
 */

public interface Node {

    void init();

    void destroy();

    boolean isAvailable();

    String desc();

    URL getUrl();
}