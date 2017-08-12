package com.alacoder.lion.tcc.sample.dubbo.consumer.service.impl;

import com.alacoder.lion.tcc.Compensable;
import com.alacoder.lion.tcc.sample.dubbo.api.ServiceException;
import com.alacoder.lion.tcc.sample.dubbo.api.service.IAccountService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("accountService")
@Compensable(interfaceClass = IAccountService.class, confirmableKey = "accountServiceConfirm", cancellableKey = "accountServiceCancel")
public class AccountServiceImpl implements IAccountService {

	@SuppressWarnings("restriction")
	@javax.annotation.Resource(name = "jdbcTemplate1")
	private JdbcTemplate jdbcTemplate;

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ServiceException.class)
	public void increaseAmount(String acctId, double amount) throws ServiceException {
		int value = this.jdbcTemplate.update("update tb_account_one set frozen = frozen + ? where acct_id = ?", amount, acctId);
		if (value != 1) {
			throw new ServiceException("ERROR!");
		}
		System.out.printf("exec increase: acct= %s, amount= %7.2f%n", acctId, amount);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ServiceException.class)
	public void decreaseAmount(String acctId, double amount) throws ServiceException {
		int value = this.jdbcTemplate.update(
				"update tb_account_one set amount = amount - ?, frozen = frozen + ? where acct_id = ?", amount, amount, acctId);
		if (value != 1) {
			throw new ServiceException("ERROR!");
		}
		System.out.printf("exec decrease: acct= %s, amount= %7.2f%n", acctId, amount);
	}

}
