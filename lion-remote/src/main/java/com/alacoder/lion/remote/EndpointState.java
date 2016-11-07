/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: ChannelState.java
 * @Package com.alacoder.lion.remote
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月16日 下午2:23:03
 * @version V1.0
 */

package com.alacoder.lion.remote;

/**
 * @ClassName: ChannelState
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月16日 下午2:23:03
 *
 */

public enum EndpointState {
	/** 未初始化状态 **/
	UNINIT(0),
	/** 初始化完成 **/
	INIT(1),
	/** 存活可用状态 **/
	ALIVE(2),
	/** 不存活可用状态 **/
	UNALIVE(3),
	/** 关闭状态 **/
	CLOSE(4);

	public final int value;

	private EndpointState(int value) {
		this.value = value;
	}

	public boolean isAliveState() {
		return this == ALIVE;
	}
	
	public boolean isUnAliveState() {
		return this == UNALIVE;
	}

	public boolean isCloseState() {
		return this == CLOSE;
	}

	public boolean isInitState() {
		return this == INIT;
	}
	
	public boolean isUnInitState() {
		return this == UNINIT;
	}
}
