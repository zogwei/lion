package com.alacoder.lion.tcc.spring.aspect;

import com.alacoder.lion.tcc.interceptor.TccTransactionInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

@Aspect
public class ConfigurableTccTransactionAspect extends TccTransactionAspect implements Ordered {

    public void init() {

        TccTransactionInterceptor tccTransactionInterceptor = new TccTransactionInterceptor();
        this.setTccTransactionInterceptor(tccTransactionInterceptor);
    }

    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
