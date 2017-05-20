/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-springsupport
 * @Title: SpringBeanUtil.java
 * @Package com.alacoder.lion.rpc.springsupport
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 下午7:22:07
 * @version V1.0
 */

package com.alacoder.lion.rpc.springsupport;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;

/**
 * @ClassName: SpringBeanUtil
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 下午7:22:07
 *
 */

public class SpringBeanUtil {
    public static final String COMMA_SPLIT_PATTERN = "\\s*[,]+\\s*";

    public static <T> List<T> getMultiBeans(BeanFactory beanFactory, String names, String pattern, Class<T> clazz) {
        String[] nameArr = names.split(pattern);
        List<T> beans = new ArrayList<T>();
        for (String name : nameArr) {
            if (name != null && name.length() > 0) {
                beans.add(beanFactory.getBean(name, clazz));
            }
        }
        return beans;
    }
}
