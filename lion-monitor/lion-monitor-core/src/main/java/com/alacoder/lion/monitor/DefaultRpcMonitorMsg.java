/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-core
 * @Title: DefaultRpcMonitorMsg.java
 * @Package com.alacoder.lion.monitor
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月14日 上午11:15:13
 * @version V1.0
 */

package com.alacoder.lion.monitor;

import java.util.Arrays;
import java.util.Date;

/**
 * @ClassName: DefaultRpcMonitorMsg
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月14日 上午11:15:13
 *
 */

public class DefaultRpcMonitorMsg implements MonitorMsg {

    private String application;
    
    private String service;

    private String method;
    
    private String group;

    private String version;
    
    private String client;
    
    private String server;
    
    private long[] metric;
    
    private Date  date;

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public long[] getMetric() {
		return metric;
	}

	public void setMetric(long[] metric) {
		this.metric = metric;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "DefaultRpcMonitorMsg [application=" + application
				+ ", service=" + service + ", method=" + method + ", group="
				+ group + ", version=" + version + ", client=" + client
				+ ", server=" + server + ", metric=" + Arrays.toString(metric)
				+ ", date=" + date + "]";
	}
	
	
    
}
