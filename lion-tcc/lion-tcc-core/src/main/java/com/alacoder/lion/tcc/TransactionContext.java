package com.alacoder.lion.tcc;

import java.io.Serializable;
import java.util.Map;

public interface TransactionContext extends Serializable {

    public TransactionXid getXid();

    public Map<String, String> getAttachments();

    public int getStatus();

}
