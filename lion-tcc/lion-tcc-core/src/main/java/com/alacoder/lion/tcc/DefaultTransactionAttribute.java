package com.alacoder.lion.tcc;

public class DefaultTransactionAttribute implements TransactionAttribute {

    private String  confirmableKey;
    private String  cancellableKey;
    private Boolean simplified;

    public String getConfirmableKey() {
        return confirmableKey;
    }

    public void setConfirmableKey(String confirmableKey) {
        this.confirmableKey = confirmableKey;
    }

    public String getCancellableKey() {
        return cancellableKey;
    }

    public void setCancellableKey(String cancellableKey) {
        this.cancellableKey = cancellableKey;
    }

    public Boolean getSimplified() {
        return simplified;
    }

    public void setSimplified(Boolean simplified) {
        this.simplified = simplified;
    }

}
