package com.alacoder.lion.tcc;

public interface TransactionManager {

    public Transaction getTransaction(TransactionAttribute transactionAttr);
    
    public Transaction getCurrentTransaction();

    public void enlistParticipant(Participant participant);

    public void cleanUp();

    public Transaction getRemoteTransaction(TransactionContext transactionContext);

    public Transaction getRemoteTransaction(TransactionXid xid);
}
