/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: FutureListener.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 下午5:13:38
 * @version V1.0
 */

package com.alacoder.lion.remote;

/**
 * @ClassName: FutureListener 用于监听Future的success和fail事件
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 下午5:13:38
 *
 */

public interface FutureListener {

	/**
     * <pre>
	 * 		建议做一些比较简单的低功耗的操作
	 * 
	 * 		注意一些反模式： 
	 * 
	 * 		1) 死循环： 
	 * 			operationComplete(Future future) {
	 * 					......
	 * 				future.addListener(this);  // 类似于这种操作，后果你懂的
	 * 					......
	 * 			}
	 * 
	 * 		2）耗资源操作或者慢操作：
	 * 			operationComplete(Future future) {
	 * 					......
	 * 				Thread.sleep(500); 
	 * 					......
	 * 			}
	 * 
	 * </pre>
     * 
     * @param future
     * @throws Exception
     */
	void operationComplete(Future future) throws Exception;
}
