/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractServiceConfig.java
 * @Package com.alacoder.lion.config
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:11:11
 * @version V1.0
 */

package com.alacoder.lion.config;

/**
 * @ClassName: AbstractServiceConfig
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:11:11
 *
 */

public abstract class AbstractServiceConfig extends AbstractInterfaceConfig {

    /**
     * 一个service可以按多个protocol提供服务，不同protocol使用不同port 利用export来设置protocol和port，格式如下：
     * protocol1:port1,protocol2:port2
     **/
    protected String export;

    /** 一般不用设置，由服务自己获取，但如果有多个ip，而只想用指定ip，则可以在此处指定 */
    protected String host;

    public String getExport() {
        return export;
    }

    public void setExport(String export) {
        this.export = export;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
