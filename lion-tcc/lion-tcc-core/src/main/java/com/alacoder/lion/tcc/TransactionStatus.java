package com.alacoder.lion.tcc;

public enum TransactionStatus {

    TRYING(1, "trying"), CONFIRMING(2, "confirming"), CANCELLING(3, "cancelling");

    private int    id;
    private String code;

    private TransactionStatus(int id, String code) {
        this.id = id;
        this.code = code;
    }

    public String code() {
        return this.code;
    }

    public int id() {
        return this.id;
    }

    public static TransactionStatus getEnum(String code) {
        if (code == null) {
            return null;
        }
        for (TransactionStatus item : TransactionStatus.values()) {
            if (item.code().equals(code)) {
                return item;
            }
        }
        return null;
    }
}