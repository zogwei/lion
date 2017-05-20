/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractRefererConfig.java
 * @Package com.alacoder.lion.config
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:12:51
 * @version V1.0
 */

package com.alacoder.lion.rpc.config;

/**
 * @ClassName: AbstractRefererConfig
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:12:51
 *
 */

public abstract class AbstractRefererConfig extends AbstractInterfaceConfig {

	private static final long serialVersionUID = 1L;
	// 服务接口的mock类SLA
    protected String mean;
    protected String p90;
    protected String p99;
    protected String p999;
    protected String errorRate;

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getP90() {
        return p90;
    }

    public void setP90(String p90) {
        this.p90 = p90;
    }

    public String getP99() {
        return p99;
    }

    public void setP99(String p99) {
        this.p99 = p99;
    }

    public String getP999() {
        return p999;
    }

    public void setP999(String p999) {
        this.p999 = p999;
    }

    public String getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(String errorRate) {
        this.errorRate = errorRate;
    }


}
