/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: TransactionExample.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月1日 下午2:18:44
 * @version V1.0
 */

package com.alacoder.lion.zk.demo;

/**
 * @ClassName: TransactionExample
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年12月1日 下午2:18:44
 *
 */

import java.util.Collection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
public class TransactionExample {
	public static void main(String[] args) {
		
	}
	public static Collection<CuratorTransactionResult> transaction(CuratorFramework client) throws Exception {
		// this example shows how to use ZooKeeper's new transactions
		Collection<CuratorTransactionResult> results = client.inTransaction().create().forPath("/a/path", "some data".getBytes())
				.and().setData().forPath("/another/path", "other data".getBytes())
				.and().delete().forPath("/yet/another/path")
				.and().commit(); // IMPORTANT!
																																// called
		for (CuratorTransactionResult result : results) {
			System.out.println(result.getForPath() + " - " + result.getType());
		}
		return results;
	}
	/*
	 * These next four methods show how to use Curator's transaction APIs in a
	 * more traditional - one-at-a-time - manner
	 */
	public static CuratorTransaction startTransaction(CuratorFramework client) {
		// start the transaction builder
		return client.inTransaction();
	}
	public static CuratorTransactionFinal addCreateToTransaction(CuratorTransaction transaction) throws Exception {
		// add a create operation
		return transaction.create().forPath("/a/path", "some data".getBytes()).and();
	}
	public static CuratorTransactionFinal addDeleteToTransaction(CuratorTransaction transaction) throws Exception {
		// add a delete operation
		return transaction.delete().forPath("/another/path").and();
	}
	public static void commitTransaction(CuratorTransactionFinal transaction) throws Exception {
		// commit the transaction
		transaction.commit();
	}
}