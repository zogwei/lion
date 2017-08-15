package com.alacoder.lion.tcc.interceptor;

import com.aben.cup.log.logging.Log;
import com.aben.cup.log.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

public class TccTransactionInterceptor {

    private final static Log logger = LogFactory.getLog(TccTransactionInterceptor.class);

    public Object interceptCompensableMethod(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("TccTransactionInterceptor interceptCompensableMethod() begin");
        return pjp.proceed();
    }
}
