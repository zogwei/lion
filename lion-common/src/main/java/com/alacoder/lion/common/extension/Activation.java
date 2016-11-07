/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-common
 * @Title: Activation.java
 * @Package com.alacoder.lion.common.extension
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 上午11:06:25
 * @version V1.0
 */

package com.alacoder.lion.common.extension;

/**
 * @ClassName: Activation
 * @Description: Spi有多个实现时，可以根据条件进行过滤、排序后再返回。
 * @author jimmy.zhong
 * @date 2016年8月5日 上午11:06:25
 *
 */
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Activation {

    /** seq号越小，在返回的list<Instance>中的位置越靠前，尽量使用 0-100以内的数字 */
    int sequence() default 20;

    /** spi 的key，获取spi列表时，根据key进行匹配，当key中存在待过滤的search-key时，匹配成功 */
    String[] key() default "";

    /** 是否支持重试的时候也调用 */
    boolean retry() default true;
}