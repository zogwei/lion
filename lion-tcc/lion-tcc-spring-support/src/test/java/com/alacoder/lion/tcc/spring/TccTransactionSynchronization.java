package com.alacoder.lion.tcc.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;


public class TccTransactionSynchronization extends TransactionSynchronizationAdapter {

    private final static Logger logger = LoggerFactory.getLogger(TccTransactionSynchronization.class);

    @Override
    public void suspend() {
        logger.info("-- suspend() -- ");
    }

    @Override
    public void resume() {
        logger.info("-- resume() -- ");
    }

    @Override
    public void flush() {
        logger.info("-- flush() -- ");
    }

    @Override
    public void beforeCommit(boolean readOnly) {
        logger.info("-- beforeCommit() -- ");
    }

    @Override
    public void beforeCompletion() {
        logger.info("-- beforeCompletion() -- ");
    }

    @Override
    public void afterCommit() {
        logger.info("-- afterCommit() -- ");
    }

    @Override
    public void afterCompletion(int status) {
        logger.info("-- afterCompletion() -- ");
    }
}
