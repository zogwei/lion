package com.alacoder.lion.tcc;

import java.util.LinkedList;
import java.util.List;

public class DefaultTransaction implements Transaction {

    private TransactionStatus  transactionStatus = null;
    
    private Long               createTime;
    private Long               lastUpdateTime;
    private Long               timeOut;

    private TransactionContext transactionContext;
    // 理论上不存在并发，无需考虑线程安全
    private List<Resource>     participantList = new LinkedList<Resource>();

    public void commit() {
        for (Resource participant : participantList) {
            participant.commit();
        }

    }

    public void rollback() {
        for (Resource participant : participantList) {
            participant.rollback();
        }
    }


}
