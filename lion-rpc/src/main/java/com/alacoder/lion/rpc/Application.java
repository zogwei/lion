/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: Application.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月17日 下午4:44:33
 * @version V1.0
 */

package com.alacoder.lion.rpc;

/**
 * @ClassName: Application
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月17日 下午4:44:33
 *
 */

public class Application {

    private String application;
    private String module;

    public Application(String application, String module) {
        this.application = application;
        this.module = module;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

}
