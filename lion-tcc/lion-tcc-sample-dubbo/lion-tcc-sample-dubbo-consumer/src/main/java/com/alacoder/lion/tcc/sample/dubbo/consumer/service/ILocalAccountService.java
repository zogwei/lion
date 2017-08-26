package com.alacoder.lion.tcc.sample.dubbo.consumer.service;

import com.alacoder.lion.tcc.Compensable;
import com.alacoder.lion.tcc.sample.dubbo.api.ServiceException;

public interface ILocalAccountService {

    @Compensable(confirmMethod = "increaseAmountConfirm", cancelMethod = "increaseAmountCancel")
	public void increaseAmount(String accountId, double amount) throws ServiceException;

    @Compensable(confirmMethod = "decreaseAmountConfirm", cancelMethod = "decreaseAmountCancel")
	public void decreaseAmount(String accountId, double amount) throws ServiceException;

}
