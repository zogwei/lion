package com.alacoder.lion.tcc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTransactionManager implements TransactionManager {

    private static final Map<Thread, Transaction> associatedTxMap = new ConcurrentHashMap<Thread, Transaction>();

    private static final Map<String, Transaction>  associatedRemoteTxMap = new ConcurrentHashMap<String, Transaction>();

    private static final DefaultTransactionManager instance        = new DefaultTransactionManager();

    public static TransactionManager getInstance() {
        return instance;
    }

    public Transaction getTransaction(TransactionAttribute transactionAttr) {
        Transaction transaction = associatedTxMap.get(Thread.currentThread());
        if (transaction == null) {
            // 当前线程不存在事务，新增一个全局事务,
            transaction = new DefaultTransaction();
            associatedTxMap.put(Thread.currentThread(), transaction);
        }

        return transaction;
    }

    public Transaction getRemoteTransaction(TransactionContext transactionContext) {
        Transaction transaction = associatedRemoteTxMap.get(transactionContext.getXid());
        if (transaction == null) {
            transaction = new DefaultTransaction();
            associatedRemoteTxMap.put(transactionContext.getXid().getBranchQualifier(), transaction);
            associatedTxMap.put(Thread.currentThread(), transaction);
        }

        return transaction;
    }

    public Transaction getRemoteTransaction(TransactionXid xid) {
        Transaction transaction = associatedRemoteTxMap.get(xid.getBranchQualifier());
        return transaction;
    }

    public Transaction getTransaction(TransactionContext transactionContext) {
        return associatedRemoteTxMap.get(transactionContext.getXid());
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

    private DefaultTransactionManager() {

    }

}
