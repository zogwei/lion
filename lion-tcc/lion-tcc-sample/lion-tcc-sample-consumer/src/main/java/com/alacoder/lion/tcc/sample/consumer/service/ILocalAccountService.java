package com.alacoder.lion.tcc.sample.consumer.service;

import com.alacoder.lion.tcc.sample.api.ServiceException;

import com.alacoder.lion.tcc.Compensable;

public interface ILocalAccountService {

    @Compensable(confirmMethod = "increaseAmountConfirm", cancelMethod = "increaseAmountCancel")
	public void increaseAmount(String accountId, double amount) throws ServiceException;

    @Compensable(confirmMethod = "decreaseAmountConfirm", cancelMethod = "decreaseAmountCancel")
	public void decreaseAmount(String accountId, double amount) throws ServiceException;

}
