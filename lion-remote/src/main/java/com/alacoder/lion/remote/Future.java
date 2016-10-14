/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: Future.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 下午5:11:24
 * @version V1.0
 */

package com.alacoder.lion.remote;

/**
 * @ClassName: Future
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 下午5:11:24
 *
 */

public interface Future {

	boolean cancel();
	boolean isCancelled();
	boolean isDone();
	boolean isSuccess();
	Object getValue();
	Exception getException();
	void addListener(FutureListener listener);
}
