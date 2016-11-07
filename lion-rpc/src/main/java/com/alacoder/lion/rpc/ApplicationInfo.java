/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: ApplicationInfo.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月17日 下午4:44:10
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;

/**
 * @ClassName: ApplicationInfo
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月17日 下午4:44:10
 *
 */

public class ApplicationInfo {

    public static final String STATISTIC = "statisitic";
    public static final ConcurrentMap<String, Application> applications = new ConcurrentHashMap<String, Application>();
    private static String CLIENT = "-client";

    public static Application getApplication(LionURL url) {
        Application application = applications.get(url.getPath());
        if (application == null && LionConstants.NODE_TYPE_REFERER.equals(url.getParameter(URLParamType.nodeType.getName()))) {
            String app = url.getParameter(URLParamType.application.getName(), URLParamType.application.getValue()) + CLIENT;
            String module = url.getParameter(URLParamType.module.getName(), URLParamType.module.getValue()) + CLIENT;

            applications.putIfAbsent(url.getPath() + CLIENT, new Application(app, module));
            application = applications.get(url.getPath() + CLIENT);
        }

        return application;
    }

    public static void addService(LionURL url) {
        Application application = applications.get(url.getPath());
        if (application == null) {
            String app = url.getParameter(URLParamType.application.getName(), URLParamType.application.getValue());
            String module = url.getParameter(URLParamType.module.getName(), URLParamType.module.getValue());

            applications.putIfAbsent(url.getPath(), new Application(app, module));
        }
    }
}
