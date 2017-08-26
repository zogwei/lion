package com.alacoder.lion.tcc;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.alacoder.lion.tcc.exception.SystemException;

import com.alacoder.lion.tcc.beanFactory.FactoryBuilder;
import com.alacoder.lion.tcc.utils.StringUtils;

/**
 * Created by changmingxie on 10/30/15.
 */
public class Terminator implements Serializable {

    private static final long serialVersionUID = -164958655471605778L;

    public Terminator() {

    }

    public Object invoke(TransactionContext transactionContext, InvocationContext invocationContext) {

        if (StringUtils.isNotEmpty(invocationContext.getMethodName())) {

            try {

                Object target = FactoryBuilder.factoryOf(invocationContext.getTargetClass()).getInstance();

                Method method = null;

                method = target.getClass().getMethod(invocationContext.getMethodName(),
                                                     invocationContext.getParameterTypes());
                //
                // FactoryBuilder.factoryOf(transactionContextEditorClass).getInstance().set(transactionContext, target,
                // method,
                // invocationContext.getArgs());

                return method.invoke(target, invocationContext.getArgs());

            } catch (Exception e) {
                throw new SystemException(e);
            }
        }
        return null;
    }

    public Object invoke(TransactionContext transactionContext, InvocationContext invocationContext, Class targetClass) {

        if (StringUtils.isNotEmpty(invocationContext.getMethodName())) {

            try {

                Object target = FactoryBuilder.factoryOf(targetClass).getInstance();

                Method method = null;

                method = target.getClass().getMethod(invocationContext.getMethodName(),
                                                     invocationContext.getParameterTypes());
                //
                // FactoryBuilder.factoryOf(transactionContextEditorClass).getInstance().set(transactionContext, target,
                // method,
                // invocationContext.getArgs());

                return method.invoke(target, invocationContext.getArgs());

            } catch (Exception e) {
                throw new SystemException(e);
            }
        }
        return null;
    }
}
