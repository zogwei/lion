package com.alacoder.lion.tcc;

import java.io.Serializable;

public class Participant implements Serializable {

    private TransactionXid                    xid;

    private InvocationContext                 confirmInvocationContext;

    private InvocationContext                 cancelInvocationContext;

    private Terminator                        terminator = new Terminator();

    private Class             targetClass;

    public Participant() {

    }

    public Participant(TransactionXid xid, InvocationContext confirmInvocationContext,
                       InvocationContext cancelInvocationContext) {
        this.xid = xid;
        this.confirmInvocationContext = confirmInvocationContext;
        this.cancelInvocationContext = cancelInvocationContext;
    }

    public Participant(InvocationContext confirmInvocationContext, InvocationContext cancelInvocationContext) {
        this.confirmInvocationContext = confirmInvocationContext;
        this.cancelInvocationContext = cancelInvocationContext;
    }

    public void setXid(TransactionXid xid) {
        this.xid = xid;
    }

    public void rollback() {
        terminator.invoke(new DefaultTransactionContext(xid, TransactionStatus.CANCELLING.id()),
                          cancelInvocationContext, targetClass);
    }

    public void commit() {
        terminator.invoke(new DefaultTransactionContext(xid, TransactionStatus.CONFIRMING.id()),
                          confirmInvocationContext, targetClass);
    }

    public Terminator getTerminator() {
        return terminator;
    }

    public TransactionXid getXid() {
        return xid;
    }

    public InvocationContext getConfirmInvocationContext() {
        return confirmInvocationContext;
    }

    public InvocationContext getCancelInvocationContext() {
        return cancelInvocationContext;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }


}
