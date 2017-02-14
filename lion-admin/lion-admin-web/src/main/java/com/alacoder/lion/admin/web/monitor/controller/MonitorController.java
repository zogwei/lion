/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-admin-web
 * @Title: MonitorController.java
 * @Package com.alacoder.lion.admin.web.monitor.controller
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月13日 下午1:48:46
 * @version V1.0
 */

package com.alacoder.lion.admin.web.monitor.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alacoder.lion.admin.web.common.BaseController;

/**
 * @ClassName: MonitorController
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月13日 下午1:48:46
 *
 */

@Controller
public class MonitorController extends BaseController{

	/**
	 * rpc monitor 
	 */
	
	List<RpcMonitorItem> data = null;
	
	DateTime nowTime = new DateTime(1993,9,3,0,0);

	long value = Math.round(Math.random() * 1000);
	
	@RequestMapping(value = "${adminPath}/monitor/rpcMonitor.json")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, Model model) {
		ModelAndView modelAndView = new ModelAndView("rpc");
		if(data == null) {
			data = new ArrayList<RpcMonitorItem>();
			for (int i = 0; i < 1000; i++) {
			    data.add(randomRpcMonitorItem());
			}
		}
		else {
			data = data.subList(5, data.size());
			for (int i = 0; i < 5; i++) {
			    data.add(randomRpcMonitorItem());
			}
		}
		
		modelAndView.addObject("data", data);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "${adminPath}/monitor/rpcMonitor")
	public ModelAndView logind(HttpServletRequest request, HttpServletResponse response, Model model) {
		ModelAndView modelAndView = new ModelAndView("rpc");
		if(data == null) {
			data = new ArrayList<RpcMonitorItem>();
			for (int i = 0; i < 1000; i++) {
			    data.add(randomRpcMonitorItem());
			}
		}
		else {
			data = data.subList(4, data.size());
			for (int i = 0; i < 5; i++) {
			    data.add(randomRpcMonitorItem());
			}
		}
		
		modelAndView.addObject(data);
		
		return modelAndView;
	}
	
	RpcMonitorItem randomRpcMonitorItem() {
		RpcMonitorItem item = new RpcMonitorItem();
		nowTime = nowTime.plusDays(1);
		value = value + Math.round(Math.random()) * 21 - 10;
		item.setName(nowTime.toString());
		List<String> values = new ArrayList<String>(2);
		item.setValue(values);
		values.add(nowTime.toString("yyyy/MM/dd"));
		values.add(value + "");
		 
		return item;
	}
	
	class RpcMonitorItem {
		String name;
		List<String> value;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<String> getValue() {
			return value;
		}
		public void setValue(List<String> value) {
			this.value = value;
		}
	};
	
}
