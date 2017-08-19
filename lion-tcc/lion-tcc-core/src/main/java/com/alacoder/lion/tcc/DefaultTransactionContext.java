package com.alacoder.lion.tcc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTransactionContext implements TransactionContext {

    private TransactionXid      xid;

    private int                 status;

    private Map<String, String> attachments = new ConcurrentHashMap<String, String>();

    public DefaultTransactionContext() {

    }

    public DefaultTransactionContext(TransactionXid xid, int status) {
        this.xid = xid;
        this.status = status;
    }

    public void setXid(TransactionXid xid) {
        this.xid = xid;
    }

    public TransactionXid getXid() {
        return xid;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
