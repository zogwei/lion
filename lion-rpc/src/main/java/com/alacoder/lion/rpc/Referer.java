/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: Referer.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午11:12:49
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.common.url.URL;

/**
 * @ClassName: Referer
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午11:12:49
 *
 */

public interface Referer<T> extends Caller<T>, Node {

    int activeRefererCount();

    URL getServiceUrl();
}
