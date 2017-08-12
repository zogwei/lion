package com.alacoder.lion.tcc.sample.dubbo.provider.jpa.springdata;

import com.alacoder.lion.tcc.sample.dubbo.provider.dao.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component("accountRepository")
public interface AccountRepository extends JpaRepository<Account, String> {

}
