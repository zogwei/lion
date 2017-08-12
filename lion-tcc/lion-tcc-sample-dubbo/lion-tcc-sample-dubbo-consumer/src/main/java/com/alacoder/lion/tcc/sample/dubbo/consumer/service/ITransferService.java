package com.alacoder.lion.tcc.sample.dubbo.consumer.service;

import com.alacoder.lion.tcc.sample.dubbo.api.ServiceException;

public interface ITransferService {

	public void transfer(String sourceAcctId, String targetAcctId, double amount) throws ServiceException;

}