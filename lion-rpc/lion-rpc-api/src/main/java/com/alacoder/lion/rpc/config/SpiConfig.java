/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: SpiConfig.java
 * @Package com.alacoder.lion.config
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 上午10:04:34
 * @version V1.0
 */

package com.alacoder.lion.rpc.config;

/**
 * @ClassName: SpiConfig
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 上午10:04:34
 *
 */

public class SpiConfig<T> {
    private String id;
    private Class<T> interfaceClass;
    private Class<T> spiClass;

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Class<T> getSpiClass() {
        return spiClass;
    }

    public void setSpiClass(Class<T> spiClass) {
        this.spiClass = spiClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}