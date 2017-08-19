package com.alacoder.lion.tcc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTransaction implements Transaction {

    private TransactionXid      xid;

    private TransactionStatus   transactionStatus;

    private volatile int        retriedCount   = 0;

    private Date                createTime     = new Date();

    private Date                lastUpdateTime = new Date();

    private long                version        = 1;

    private List<Participant>   participantList = new ArrayList<Participant>();

    private Map<String, Object> attachments    = new ConcurrentHashMap<String, Object>();

    public DefaultTransaction() {
        this.xid = new TransactionXid();
    }

    public DefaultTransaction(TransactionXid xid) {
        this.xid = xid;
    }

    // TODO 考虑 参与者先后提交问题
    public void commit() {
        for (Participant participant : participantList) {
            participant.commit();
        }

    }

    public void rollback() {
        for (Participant participant : participantList) {
            participant.rollback();
        }
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
    }

    public void enlistParticipant(Participant participant) {
        this.participantList.add(participant);
    }

    public TransactionXid getXid() {
        return xid;
    }

    public void setXid(TransactionXid xid) {
        this.xid = xid;
    }

    public int getRetriedCount() {
        return retriedCount;
    }

    public void setRetriedCount(int retriedCount) {
        this.retriedCount = retriedCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }

}
