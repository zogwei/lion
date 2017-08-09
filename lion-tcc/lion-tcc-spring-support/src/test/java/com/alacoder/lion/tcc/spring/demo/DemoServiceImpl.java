package com.alacoder.lion.tcc.spring.demo;

import javax.annotation.Resource;

import com.alacoder.lion.tcc.spring.TccService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("demoService")
@Transactional
public class DemoServiceImpl implements DemoService {

    private final static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Resource
    private JdbcTemplate        jdbcTemplate;

    @Resource
    private TccService          tccService;

    public boolean addData(Long id) {
        tccService.start("11", null, null);
        jdbcTemplate.execute("INSERT INTO t_tcc_demo (`id`, `name`) VALUES ('" + id + "', 'name')");

        // throw new RuntimeException();

        return true;
    }

}
