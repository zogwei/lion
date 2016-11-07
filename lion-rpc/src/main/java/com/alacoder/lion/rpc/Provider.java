/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: Provider.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午4:25:18
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.common.extension.Spi;
import com.alacoder.lion.common.extension.Scope;

/**
 * @ClassName: Provider
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午4:25:18
 *
 */
@Spi(scope = Scope.PROTOTYPE)
public interface Provider<T> extends Caller<T> {

	 Class<T> getInterface();
}
