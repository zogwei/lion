package com.alacoder.lion.tcc.spring.beanFactory;

import java.util.Map;

import com.alacoder.lion.tcc.beanFactory.BeanFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by changmingxie on 11/22/15.
 */
public class SpringBeanFactory implements BeanFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean isFactoryOf(Class clazz) {
        Map map = this.applicationContext.getBeansOfType(clazz);
        return map.size() > 0;
    }

    @Override
    public <T> T getBean(Class<T> var1) {
        Map<String, T> beans = applicationContext.getBeansOfType(var1);

        return this.applicationContext.getBean(var1);
    }

}
