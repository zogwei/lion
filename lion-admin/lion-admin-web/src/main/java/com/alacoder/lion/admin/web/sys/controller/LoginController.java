/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-admin-web
 * @Title: LoginController.java
 * @Package com.alacoder.lion.admin.web.sys.controller
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月24日 下午2:46:04
 * @version V1.0
 */

package com.alacoder.lion.admin.web.sys.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alacoder.lion.admin.web.common.BaseController;
import com.alacoder.lion.admin.web.common.utils.CacheUtils;
import com.alacoder.lion.admin.web.common.utils.CookieUtils;
import com.alacoder.lion.admin.web.common.utils.StringUtils;
import com.alacoder.lion.admin.web.sys.service.SystemService;
import com.google.common.collect.Maps;

/**
 * 登录Controller
 * @author jimmy
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private SystemService systemService;
	
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
	
		systemService.login();
		
		return "admin/index.jsp";
	}


	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request, HttpServletResponse response) {

		
		return "redirect:" + adminPath + "/login";
	}
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
}
