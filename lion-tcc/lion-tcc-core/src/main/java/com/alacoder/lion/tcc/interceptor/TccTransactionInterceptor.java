package com.alacoder.lion.tcc.interceptor;

import java.lang.reflect.Method;

import com.aben.cup.log.logging.Log;
import com.aben.cup.log.logging.LogFactory;
import com.alacoder.lion.tcc.Compensable;
import com.alacoder.lion.tcc.DefaultTransactionManager;
import com.alacoder.lion.tcc.InvocationContext;
import com.alacoder.lion.tcc.Participant;
import com.alacoder.lion.tcc.Transaction;
import com.alacoder.lion.tcc.TransactionManager;
import com.alacoder.lion.tcc.TransactionStatus;
import com.alacoder.lion.tcc.TransactionXid;
import com.alacoder.lion.tcc.utils.CompensableUtils;
import com.alacoder.lion.tcc.utils.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;

public class TccTransactionInterceptor {

    private final static Log logger = LogFactory.getLog(TccTransactionInterceptor.class);

    private TransactionManager transactionManager = DefaultTransactionManager.getInstance();


    public Object interceptCompensableMethod(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("TccTransactionInterceptor interceptCompensableMethod() begin");

        // param check
        Method compensableMethod = CompensableUtils.getCompensableMethod(pjp);

        Transaction transaction = createTransactionIfNecessary();
        enlistParticipant(pjp);
        Object retVal = null;
        try {
            // This is an around advice: Invoke the next interceptor in the chain.
            // This will normally result in a target object being invoked.
            retVal = pjp.proceed();
            commitTransaction(transaction);

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
        logger.debug("TccTransactionInterceptor completeTransactionAfterThrowing() begin");
        transaction.setTransactionStatus(TransactionStatus.CANCELLING);
        transaction.rollback();
    }

    private void cleanupTransaction(Transaction transaction) {
        transactionManager.cleanUp();
    }

    private void commitTransaction(Transaction transaction) {
        logger.debug("TccTransactionInterceptor commitTransaction() begin");
        transaction.setTransactionStatus(TransactionStatus.CONFIRMING);
        transaction.commit();

    }

    private void commitTransactionAfterReturning(Transaction transaction) {

    }

    // private TransactionAttribute getTransactionAttribute(ProceedingJoinPoint pjp) {
    // TransactionAttribute ret = null;
    //
    // return ret;
    // }
    //
    private Transaction createTransactionIfNecessary() {
        return transactionManager.getTransaction();
    }


    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    private void enlistParticipant(ProceedingJoinPoint pjp) {
        Method method = CompensableUtils.getCompensableMethod(pjp);
        Compensable compensable = method.getAnnotation(Compensable.class);

        String confirmMethodName = compensable.confirmMethod();
        String cancelMethodName = compensable.cancelMethod();

        Transaction transaction = transactionManager.getCurrentTransaction();
        TransactionXid xid = new TransactionXid(transaction.getXid().getGlobalTransactionId());

        Class targetClass = ReflectionUtils.getDeclaringType(pjp.getTarget().getClass(), method.getName(),
                                                             method.getParameterTypes());



        InvocationContext confirmInvocation = new InvocationContext(targetClass, confirmMethodName,
                                                                    method.getParameterTypes(), pjp.getArgs());

        InvocationContext cancelInvocation = new InvocationContext(targetClass, cancelMethodName,
                                                                   method.getParameterTypes(), pjp.getArgs());

        Participant participant = new Participant(xid, confirmInvocation, cancelInvocation);

        participant.setTargetClass(pjp.getTarget().getClass());

        transactionManager.enlistParticipant(participant);

    }

}
