/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: LoadBalance.java
 * @Package com.alacoder.lion.rpc.ha
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午3:12:48
 * @version V1.0
 */

package com.alacoder.lion.rpc.ha;

import java.util.List;

import com.alacoder.lion.common.extension.Spi;
import com.alacoder.lion.common.extension.Scope;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.rpc.Referer;

/**
 * @ClassName: LoadBalance
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午3:12:48
 *
 */

@Spi(scope = Scope.PROTOTYPE)
public interface LoadBalance<T> {

    void onRefresh(List<Referer<T>> referers);

    @SuppressWarnings("rawtypes")
	Referer<T> select(Request request);

    @SuppressWarnings("rawtypes")
	void selectToHolder(Request request, List<Referer<T>> refersHolder);

    void setWeightString(String weightString);

}