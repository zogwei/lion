package com.alacoder.lion.tcc.sample.dubbo.consumer.service.impl;

import com.alacoder.lion.tcc.Compensable;
import com.alacoder.lion.tcc.sample.dubbo.api.ServiceException;
import com.alacoder.lion.tcc.sample.dubbo.api.service.IAccountService;
import com.alacoder.lion.tcc.sample.dubbo.consumer.service.ITransferService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("genericTransferService")
public class GenericTransferServiceImpl implements ITransferService {

	@SuppressWarnings("restriction")
	@javax.annotation.Resource(name = "jdbcTemplate2")
	private JdbcTemplate jdbcTemplate;
	@SuppressWarnings("restriction")
	@javax.annotation.Resource(name = "remoteAccountService")
	private IAccountService remoteAccountService;

	@Transactional(rollbackFor = ServiceException.class)
    @Compensable(interfaceClass = ITransferService.class, confirmableKey = "transferServiceConfirm", cancellableKey = "transferServiceCancel")
	public void transfer(String sourceAcctId, String targetAcctId, double amount) throws ServiceException {

		this.remoteAccountService.decreaseAmount(sourceAcctId, amount);
		this.increaseAmount(targetAcctId, amount);

		 throw new ServiceException("rollback");
	}

    @Transactional(rollbackFor = ServiceException.class)
    public void transferServiceConfirm(String sourceAcctId, String targetAcctId, double amount) throws ServiceException {
        int value = this.jdbcTemplate.update("update tb_account_two set amount = amount + ?, frozen = frozen - ? where acct_id = ?",
                                             amount, amount, targetAcctId);
        if (value != 1) {
            throw new ServiceException("ERROR!");
        }
        System.out.printf("done increase: acct= %s, amount= %7.2f%n", targetAcctId, amount);
    }

    @Transactional(rollbackFor = ServiceException.class)
    public void transferServiceCancel(String sourceAcctId, String targetAcctId, double amount) throws ServiceException {
        int value = this.jdbcTemplate.update("update tb_account_two set frozen = frozen - ? where acct_id = ?", amount,
                                             targetAcctId);
        if (value != 1) {
            throw new ServiceException("ERROR!");
        }
        System.out.printf("exec decrease: acct= %s, amount= %7.2f%n", targetAcctId, amount);
    }

	private void increaseAmount(String acctId, double amount) throws ServiceException {
		int value = this.jdbcTemplate.update("update tb_account_two set frozen = frozen + ? where acct_id = ?", amount, acctId);
		if (value != 1) {
			throw new ServiceException("ERROR!");
		}
		System.out.printf("exec increase: acct= %s, amount= %7.2f%n", acctId, amount);
	}

}
