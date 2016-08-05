/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-common
 * @Title: ActivationComparator.java
 * @Package com.alacoder.lion.common.extension
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 上午11:07:03
 * @version V1.0
 */

package com.alacoder.lion.common.extension;

/**
 * @ClassName: ActivationComparator
 * @Description: Priority comparator
 * @author jimmy.zhong
 * @date 2016年8月5日 上午11:07:03
 *
 */
import java.util.Comparator;

public class ActivationComparator<T> implements Comparator<T> {

    /**
     * sequence 大的排在后面,如果没有设置sequence的排到最前面
     */
    @Override
    public int compare(T o1, T o2) {
        Activation p1 = o1.getClass().getAnnotation(Activation.class);
        Activation p2 = o2.getClass().getAnnotation(Activation.class);
        if (p1 == null) {
            return 1;
        } else if (p2 == null) {
            return -1;
        } else {
            return p1.sequence() - p2.sequence();
        }
    }


}

