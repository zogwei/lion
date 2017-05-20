/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-admin-web
 * @Title: DataEntity.java
 * @Package com.alacoder.lion.admin.web.common
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月3日 下午3:31:21
 * @version V1.0
 */

package com.alacoder.lion.admin.web.common;

/**
 * @ClassName: DataEntity
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月3日 下午3:31:21
 *
 */
public abstract class DataEntity<T> {

	/**
	 * 实体编号（唯一标识）
	 */
	protected String id;
	
	public DataEntity(){
		
	}
	
	public DataEntity(String id) {
		this.id = id;
	}


	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		 this.id = id;
	}
}
