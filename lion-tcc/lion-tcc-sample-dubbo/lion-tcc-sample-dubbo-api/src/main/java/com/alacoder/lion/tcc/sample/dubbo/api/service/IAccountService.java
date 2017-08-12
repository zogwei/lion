package com.alacoder.lion.tcc.sample.dubbo.api.service;

import com.alacoder.lion.tcc.sample.dubbo.api.ServiceException;

public interface IAccountService {

	public void increaseAmount(String accountId, double amount) throws ServiceException;

	public void decreaseAmount(String accountId, double amount) throws ServiceException;

}
