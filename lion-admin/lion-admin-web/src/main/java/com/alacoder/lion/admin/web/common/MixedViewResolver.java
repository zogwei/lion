/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-admin-web
 * @Title: MixedViewResolver.java
 * @Package com.alacoder.lion.admin.web.common
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月24日 下午2:22:33
 * @version V1.0
 */

package com.alacoder.lion.admin.web.common;

/**
 * @ClassName: MixedViewResolver
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月24日 下午2:22:33
 *
 */
import java.util.Locale;
import java.util.Map;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
* 说明： 多视图处理器
*
* @author  赵磊
* @version 创建时间：2011-8-19 上午09:41:09
*/
public class MixedViewResolver implements ViewResolver{
    private Map<String,ViewResolver> resolvers;

    public void setResolvers(Map<String, ViewResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public View resolveViewName(String viewName,Locale locale) throws Exception{
        int n=viewName.lastIndexOf(".");
        if(n!=-1){
            //取出扩展名
            String suffix=viewName.substring(n+1);
            //取出对应的ViewResolver
            ViewResolver resolver=resolvers.get(suffix);
            if(resolver==null){
                throw new RuntimeException("No ViewResolver for "+suffix);
            }
            return  resolver.resolveViewName(viewName, locale);
        }else{
            ViewResolver resolver=resolvers.get("jsp");
            return  resolver.resolveViewName(viewName, locale);
        }
    }
}