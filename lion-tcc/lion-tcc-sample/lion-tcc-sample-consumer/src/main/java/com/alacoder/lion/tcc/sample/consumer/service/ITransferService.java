package com.alacoder.lion.tcc.sample.consumer.service;

import com.alacoder.lion.tcc.sample.api.ServiceException;

public interface ITransferService {

	public void transfer(String sourceAcctId, String targetAcctId, double amount) throws ServiceException;

    public void transferServiceConfirm(String sourceAcctId, String targetAcctId, double amount) throws ServiceException;

    public void transferServiceCancel(String sourceAcctId, String targetAcctId, double amount) throws ServiceException;
}
