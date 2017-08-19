package com.alacoder.lion.tcc;

public interface Transaction {

    public void commit();

    public void rollback();
}
