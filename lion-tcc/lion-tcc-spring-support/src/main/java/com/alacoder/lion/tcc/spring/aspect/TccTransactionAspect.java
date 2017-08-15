package com.alacoder.lion.tcc.spring.aspect;

import com.alacoder.lion.tcc.interceptor.TccTransactionInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public abstract class TccTransactionAspect {

    private TccTransactionInterceptor tccTransactionInterceptor;
    
    @Pointcut("@annotation(com.alacoder.lion.tcc.Compensable)")
    public void compensableService(){
        
    }
    
    @Around("compensableService()")
    public Object intercepetHander(ProceedingJoinPoint pjp) throws Throwable {
        return tccTransactionInterceptor.interceptCompensableMethod(pjp);
    }
    
    public void setTccTransactionInterceptor(TccTransactionInterceptor tccTransactionInterceptor){
        this.tccTransactionInterceptor = tccTransactionInterceptor;
    }
}
