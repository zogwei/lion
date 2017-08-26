package com.alacoder.lion.tcc.sample.api.service;

import com.alacoder.lion.tcc.sample.api.ServiceException;

import com.alacoder.lion.tcc.Compensable;

public interface IAccountService {

    @Compensable(confirmMethod = "increaseAmountConfirm", cancelMethod = "increaseAmountCancel")
	public void increaseAmount(String accountId, double amount) throws ServiceException;

    public void increaseAmountConfirm(String accountId, double amount) throws ServiceException;

    public void increaseAmountCancel(String accountId, double amount) throws ServiceException;

    @Compensable(confirmMethod = "decreaseAmountConfirm", cancelMethod = "decreaseAmountCancel")
	public void decreaseAmount(String accountId, double amount) throws ServiceException;

    public void decreaseAmountConfirm(String accountId, double amount) throws ServiceException;

    public void decreaseAmountCancel(String accountId, double amount) throws ServiceException;

}
