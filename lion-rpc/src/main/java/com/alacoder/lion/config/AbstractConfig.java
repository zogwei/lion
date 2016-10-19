/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractConfig.java
 * @Package com.alacoder.lion.config
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:10:05
 * @version V1.0
 */

package com.alacoder.lion.config;

import java.io.Serializable;

/**
 * @ClassName: AbstractConfig
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:10:05
 *
 */

public abstract class AbstractConfig implements Serializable {

    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
