package com.alacoder.lion.tcc.sample.dubbo.provider.svc4jpa.cancel;

import com.alacoder.lion.tcc.sample.dubbo.api.ServiceException;
import com.alacoder.lion.tcc.sample.dubbo.api.service.IAccountService;
import com.alacoder.lion.tcc.sample.dubbo.provider.dao.IAccountDao;
import com.alacoder.lion.tcc.sample.dubbo.provider.dao.model.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("accountServiceCancel4JPA")
public class AccountServiceCancel4JPA implements IAccountService {

	@SuppressWarnings("restriction")
	@javax.annotation.Resource(name = "accountDao")
	private IAccountDao accountDao;

	@Transactional(rollbackFor = ServiceException.class)
	public void increaseAmount(String acctId, double amount) throws ServiceException {
		Account account = this.accountDao.findById(acctId);
		account.setFrozen(account.getFrozen() - amount); // 真实业务中, 请考虑设置乐观锁/悲观锁, 以便并发操作时导致数据不一致
		this.accountDao.update(account);
		System.out.printf("[jpa] undo increase: acct= %s, amount= %7.2f%n", acctId, amount);
	}

	@Transactional(rollbackFor = ServiceException.class)
	public void decreaseAmount(String acctId, double amount) throws ServiceException {
		Account account = this.accountDao.findById(acctId);
		account.setAmount(account.getAmount() + amount); // 真实业务中, 请考虑设置乐观锁/悲观锁, 以便并发操作时导致数据不一致
		account.setFrozen(account.getFrozen() - amount);
		this.accountDao.update(account);
		System.out.printf("[jpa] undo decrease: acct= %s, amount= %7.2f%n", acctId, amount);
	}

}
