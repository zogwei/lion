package com.alacoder.lion.tcc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTransactionManager implements TransactionManager {

    private final Map<Thread, Transaction> associatedTxMap = new ConcurrentHashMap<Thread, Transaction>();

    public Transaction getTransaction(TransactionAttribute transactionAttr) {
        Transaction transaction = associatedTxMap.get(Thread.currentThread());
        if (transaction != null) {
            // 已经存在事务，作为参与方加入事务
        } else {
            // 当前线程不存在事务，新增一个全局事务

        }

        return transaction;
    }

}
