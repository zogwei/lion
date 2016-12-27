/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: FutureState.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 下午5:17:11
 * @version V1.0
 */

package com.alacoder.lion.remote;

/**
 * @ClassName: FutureState
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月11日 下午5:17:11
 *
 */

public enum FutureState {
	/** the task is doing **/
	DOING(0),
	/** the task is done **/
	DONE(1),
	/** ths task is cancelled **/
	CANCELLED(2);

	public final int value;

	private FutureState(int value) {
		this.value = value;
	}

	public boolean isCancelledState() {
		return this == CANCELLED;
	}

	public boolean isDoneState() {
		return this == DONE;
	}

	public boolean isDoingState() {
		return this == DOING;
	}
}
