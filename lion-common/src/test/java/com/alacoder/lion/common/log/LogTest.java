package com.alacoder.lion.common.log;

import com.aben.cup.log.logging.LogFactory;

import com.aben.cup.log.logging.Log;


public class LogTest {

	private final static Log logger = LogFactory.getLog(LogTest.class);
	
	public static void main(String[] args) {
		logger.error("test");
	}

}
