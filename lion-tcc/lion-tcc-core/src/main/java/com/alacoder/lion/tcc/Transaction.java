package com.alacoder.lion.tcc;


public interface Transaction {

    public void commit();

    public void rollback();

    public void enlistParticipant(Participant participant);

    public TransactionXid getXid();

    public TransactionStatus getTransactionStatus();
}
