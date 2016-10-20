/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: ConfigDesc.java
 * @Package com.alacoder.lion.config
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 上午9:49:14
 * @version V1.0
 */

package com.alacoder.lion.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: ConfigDesc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 上午9:49:14
 *
 */

/**
 * 
 * 对配置参数的描述，用于通过配置方法进行配置属性自动装载
 *
 * @author fishermen
 * @version V1.0 created at: 2013-6-6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConfigDesc {

    String key() default "";

    boolean excluded() default false;

    boolean required() default false;
}
