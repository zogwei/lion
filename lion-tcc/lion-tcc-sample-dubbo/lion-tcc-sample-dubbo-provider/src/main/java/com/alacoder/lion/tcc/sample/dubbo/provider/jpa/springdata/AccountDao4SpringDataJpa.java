package com.alacoder.lion.tcc.sample.dubbo.provider.jpa.springdata;

import com.alacoder.lion.tcc.sample.dubbo.provider.dao.IAccountDao;
import com.alacoder.lion.tcc.sample.dubbo.provider.dao.model.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("accountDao")
public class AccountDao4SpringDataJpa implements IAccountDao {
	@Autowired
	private AccountRepository repository;

	public Account findById(String identifier) {
		return this.repository.findOne(identifier);
	}

	public void insert(Account account) {
		this.repository.saveAndFlush(account);
	}

	public void update(Account account) {
		this.repository.saveAndFlush(account);
	}

	public void delete(Account account) {
		this.repository.delete(account);
	}

}
