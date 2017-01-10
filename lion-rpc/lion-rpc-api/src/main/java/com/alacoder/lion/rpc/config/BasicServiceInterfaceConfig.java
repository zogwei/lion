/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: BasicServiceInterfaceConfig.java
 * @Package com.alacoder.lion.config
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:12:13
 * @version V1.0
 */

package com.alacoder.lion.rpc.config;

/**
 * @ClassName: BasicServiceInterfaceConfig
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:12:13
 *
 */

public class BasicServiceInterfaceConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = 6195653568016977636L;
	/** 是否默认配置 */
    private Boolean isDefault;

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean isDefault() {
        return isDefault;
    }
}
