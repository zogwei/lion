/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-common
 * @Title: Spi.java
 * @Package com.alacoder.lion.common.extension
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 上午11:12:13
 * @version V1.0
 */

package com.alacoder.lion.common.extension;

/**
 * @ClassName: Spi
 * @Description: TODO
 * @author jimmy.zhong
 * @date 2016年8月5日 上午11:12:13
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
public @interface Spi {

    Scope scope() default Scope.PROTOTYPE;

}
