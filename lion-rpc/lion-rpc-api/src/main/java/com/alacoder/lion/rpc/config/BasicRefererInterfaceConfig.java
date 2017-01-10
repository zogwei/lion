/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: BasicRefererInterfaceConfig.java
 * @Package com.alacoder.lion.config
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:12:36
 * @version V1.0
 */

package com.alacoder.lion.rpc.config;

/**
 * @ClassName: BasicRefererInterfaceConfig
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:12:36
 *
 */

public class BasicRefererInterfaceConfig extends AbstractRefererConfig {
	
	private static final long serialVersionUID = 1L;
	/** 是否默认配置 */
    private Boolean isDefault;

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean isDefault() {
        return isDefault;
    }
}
