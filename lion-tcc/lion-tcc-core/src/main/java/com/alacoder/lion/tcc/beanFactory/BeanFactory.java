package com.alacoder.lion.tcc.beanFactory;

public interface BeanFactory {

    <T> T getBean(Class<T> var1);

    <T> boolean isFactoryOf(Class<T> clazz);
}