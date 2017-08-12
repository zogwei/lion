package com.alacoder.lion.tcc.sample.dubbo.provider.dao;

import com.alacoder.lion.tcc.sample.dubbo.provider.dao.model.Account;

public interface IAccountDao {

	public Account findById(String identifier);

	public void insert(Account account);

	public void update(Account account);

	public void delete(Account account);

}
