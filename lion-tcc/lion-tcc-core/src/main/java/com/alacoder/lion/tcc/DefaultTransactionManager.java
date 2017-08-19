package com.alacoder.lion.tcc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTransactionManager implements TransactionManager {

    private final Map<Thread, Transaction> associatedTxMap = new ConcurrentHashMap<Thread, Transaction>();

    public Transaction getTransaction(TransactionAttribute transactionAttr) {
        Transaction transaction = associatedTxMap.get(Thread.currentThread());
        if (transaction == null) {
            // 当前线程不存在事务，新增一个全局事务,
            transaction = new DefaultTransaction();
            associatedTxMap.put(Thread.currentThread(), transaction);
        }

        return transaction;
    }

    public Transaction getCurrentTransaction() {
        return associatedTxMap.get(Thread.currentThread());
    }

    public void enlistParticipant(Participant participant) {
        getCurrentTransaction().enlistParticipant(participant);
    }

    public void cleanUp() {
        associatedTxMap.remove(Thread.currentThread());
    }

}
