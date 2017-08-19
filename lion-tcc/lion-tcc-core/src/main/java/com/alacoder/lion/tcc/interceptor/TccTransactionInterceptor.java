package com.alacoder.lion.tcc.interceptor;

import java.lang.reflect.Method;

import com.aben.cup.log.logging.Log;
import com.aben.cup.log.logging.LogFactory;
import com.alacoder.lion.tcc.DefaultTransactionManager;
import com.alacoder.lion.tcc.Transaction;
import com.alacoder.lion.tcc.TransactionAttribute;
import com.alacoder.lion.tcc.TransactionManager;
import com.alacoder.lion.tcc.utils.CompensableUtils;
import org.aspectj.lang.ProceedingJoinPoint;

public class TccTransactionInterceptor {

    private final static Log logger = LogFactory.getLog(TccTransactionInterceptor.class);

    private TransactionManager transactionManager = new DefaultTransactionManager();


    public Object interceptCompensableMethod(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("TccTransactionInterceptor interceptCompensableMethod() begin");

        // param check
        Method compensableMethod = CompensableUtils.getCompensableMethod(pjp);

        TransactionAttribute transactionAttr = getTransactionAttribute(pjp);

        Transaction transaction = createTransactionIfNecessary(transactionAttr);
        Object retVal = null;
        try {
            // This is an around advice: Invoke the next interceptor in the chain.
            // This will normally result in a target object being invoked.
            retVal = pjp.proceed();
        } catch (Throwable ex) {
            // target invocation exception
            completeTransactionAfterThrowing(transaction, ex);
            throw ex;
        } finally {
            cleanupTransaction(transaction);
        }

        commitTransactionAfterReturning(transaction);

        return retVal;

    }

    private void completeTransactionAfterThrowing(Transaction transaction, Throwable ex) {
        transaction.rollback();
    }

    private void cleanupTransaction(Transaction transaction) {

    }

    private void commitTransactionAfterReturning(Transaction transaction) {
        transaction.commit();
    }

    private TransactionAttribute getTransactionAttribute(ProceedingJoinPoint pjp) {
        TransactionAttribute ret = null;

        return ret;
    }

    private Transaction createTransactionIfNecessary(TransactionAttribute transactionAtt) {
        return transactionManager.getTransaction(transactionAtt);
    }


    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

}
