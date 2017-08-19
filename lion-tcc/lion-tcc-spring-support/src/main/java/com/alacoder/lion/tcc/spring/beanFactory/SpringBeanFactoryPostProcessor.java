package com.alacoder.lion.tcc.spring.beanFactory;

import com.alacoder.lion.tcc.beanFactory.BeanFactory;
import com.alacoder.lion.tcc.beanFactory.FactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class SpringBeanFactoryPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();

        if (applicationContext.getParent() == null) {
            FactoryBuilder.registerBeanFactory(applicationContext.getBean(BeanFactory.class));
        }
    }
}
