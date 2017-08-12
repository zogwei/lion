package com.alacoder.lion.tcc.sample.dubbo.consumer.service.cancel;

import com.alacoder.lion.tcc.sample.dubbo.api.ServiceException;
import com.alacoder.lion.tcc.sample.dubbo.consumer.service.ITransferService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("transferServiceCancel")
public class TransferServiceCancel implements ITransferService {

	@SuppressWarnings("restriction")
	@javax.annotation.Resource(name = "jdbcTemplate2")
	private JdbcTemplate jdbcTemplate;

	@Transactional(rollbackFor = ServiceException.class)
	public void transfer(String sourceAcctId, String targetAcctId, double amount) throws ServiceException {
		int value = this.jdbcTemplate.update("update tb_account_two set frozen = frozen - ? where acct_id = ?", amount,
				targetAcctId);
		if (value != 1) {
			throw new ServiceException("ERROR!");
		}
		System.out.printf("exec decrease: acct= %s, amount= %7.2f%n", targetAcctId, amount);
	}

}
