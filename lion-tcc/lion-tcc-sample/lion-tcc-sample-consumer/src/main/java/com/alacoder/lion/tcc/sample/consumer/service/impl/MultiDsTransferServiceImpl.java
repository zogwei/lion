package com.alacoder.lion.tcc.sample.consumer.service.impl;

import com.alacoder.lion.tcc.sample.api.ServiceException;

import com.alacoder.lion.tcc.sample.consumer.service.ILocalAccountService;
import com.alacoder.lion.tcc.sample.consumer.service.ITransferService;
import com.alacoder.lion.tcc.Compensable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("multiDsTransferService")
public class MultiDsTransferServiceImpl implements ITransferService {

	@SuppressWarnings("restriction")
	@javax.annotation.Resource(name = "accountService")
    private ILocalAccountService nativeAccountService;
	@SuppressWarnings("restriction")
	@javax.annotation.Resource(name = "jdbcTemplate2")
	private JdbcTemplate jdbcTemplate;

	@Transactional(rollbackFor = ServiceException.class)
    @Compensable(confirmMethod = "transferServiceConfirm", cancelMethod = "transferServiceCancel")
	public void transfer(String sourceAcctId, String targetAcctId, double amount) throws ServiceException {

		this.nativeAccountService.decreaseAmount(sourceAcctId, amount);
		this.increaseAmount(targetAcctId, amount);

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
