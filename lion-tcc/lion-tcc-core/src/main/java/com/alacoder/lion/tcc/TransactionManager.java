package com.alacoder.lion.tcc;

public interface TransactionManager {

    public Transaction getTransaction(TransactionAttribute transactionAttr);
}
