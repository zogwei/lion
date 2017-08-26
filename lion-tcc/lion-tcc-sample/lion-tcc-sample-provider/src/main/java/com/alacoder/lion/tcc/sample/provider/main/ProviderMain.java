package com.alacoder.lion.tcc.sample.provider.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProviderMain {

	static ClassPathXmlApplicationContext context = null;

	public static void main(String... args) throws Throwable {
		System.out.println("begin sample-provider started!");
		context = new ClassPathXmlApplicationContext("application.xml");
		context.start();

		System.out.println("sample-provider started!");
        Thread.sleep(1000 * 1000);
	}

}
