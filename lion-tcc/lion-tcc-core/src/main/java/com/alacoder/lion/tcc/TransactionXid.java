package com.alacoder.lion.tcc;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.UUID;

public class TransactionXid implements Serializable {

    private static final long serialVersionUID = -1;

    private int formatId = 1;

    private String            globalTransactionId;

    private String            branchQualifier;

    public TransactionXid() {
        globalTransactionId = String.valueOf(uuidToByteArray(UUID.randomUUID()));
        branchQualifier = String.valueOf(uuidToByteArray(UUID.randomUUID()));
    }

    public TransactionXid(String globalTransactionId) {
        globalTransactionId = globalTransactionId;
        branchQualifier = String.valueOf(uuidToByteArray(UUID.randomUUID()));
    }

    public static byte[] uuidToByteArray(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID byteArrayToUUID(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    public String getGlobalTransactionId() {
        return globalTransactionId;
    }

    public void setGlobalTransactionId(String globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
    }

    public String getBranchQualifier() {
        return branchQualifier;
    }

    public void setBranchQualifier(String branchQualifier) {
        this.branchQualifier = branchQualifier;
    }

    public boolean equals(TransactionXid xid) {
        if (xid == null) {
            return false;
        }
        return this.globalTransactionId.equals(xid.getGlobalTransactionId())
               && this.getBranchQualifier().equals(xid.getBranchQualifier());
    }

    public int hashcode() {
        return this.globalTransactionId.hashCode() + this.getBranchQualifier().hashCode();
    }

}
