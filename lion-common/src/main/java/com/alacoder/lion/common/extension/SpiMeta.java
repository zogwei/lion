/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-common
 * @Title: SpiMeta.java
 * @Package com.alacoder.lion.common.extension
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 上午11:12:31
 * @version V1.0
 */

package com.alacoder.lion.common.extension;

/**
 * @ClassName: SpiMeta
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 上午11:12:31
 *
 */
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SpiMeta {
    String name() default "";
}
