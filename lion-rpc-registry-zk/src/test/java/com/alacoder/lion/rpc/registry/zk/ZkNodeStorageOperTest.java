/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc-registry-zk
 * @Title: ZkNodeStorageOperTest.java
 * @Package com.alacoder.lion.rpc.registry.zk
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月22日 下午4:21:07
 * @version V1.0
 */

package com.alacoder.lion.rpc.registry.zk;

/**
 * @ClassName: ZkNodeStorageOperTest
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年11月22日 下午4:21:07
 *
 */

public class ZkNodeStorageOperTest {


	public static void main(String[] args) {
		ZkConfiguration zkconf = new ZkConfiguration("localhost:2181","rpc/zk",1000,1000,3);
		CuratorOper zkOper = new CuratorOper(zkconf);
		
		//新增
		zkOper.persist("/keyPersisit", "valuePersisit");
		zkOper.persistSequential("/sequential/persistSequential", "valuepersistSequential");
		
		zkOper.persist("/keyPersisit/subpath1", "valueSubPersisit");
		zkOper.persist("/keyPersisit/subpath2", "valueSubPersisit");
		zkOper.persist("/keyPersisit/subpath1/subsub", "valueSubPersisit");
		System.out.println("key : /keyPersisit" + " value  list: " +  zkOper.getChildrenKeys("/keyPersisit"));
		
		zkOper.persist("/keyPersisit/subpath2/subsubkey", "valueSubsubPersisit");
		zkOper.persistEphemeral("/keypersistEphemeral-1", "valuepersistEphemeral-1");
		zkOper.persistEphemeralSequential("/EphemeralSequential-value/keypersistEphemeralSequential","value");
		zkOper.persistEphemeralSequential("/EphemeralSequential/keypersistEphemeralSequential");
		zkOper.persistEphemeralSequential("/EphemeralSequential/keypersistEphemeralSequential");
		
		System.out.println("key : /keypersistEphemeral-1" + " value : " +  zkOper.get("/keypersistEphemeral-1"));
		System.out.println("key : /EphemeralSequential" + " value  list: " +  zkOper.getChildrenKeys("/EphemeralSequential"));
		
		
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		zkOper.close();
		
		zkOper = new CuratorOper(zkconf);
		System.out.println("key : /keyPersisit" + " value : " +  zkOper.get("/keyPersisit"));
		System.out.println("key : /keyPersisit" + " child value  : " +  zkOper.getChildrenKeys("/keyPersisit"));
		
//		System.out.println("key : /keypersistEphemeral-1" + " value : " +  zkOper.get("/keypersistEphemeral-1"));
////		System.out.println("key : /EphemeralSequential" + " value : " +  zkOper.get("/keypersistEphemeralSequential"));
//		System.out.println("key : /EphemeralSequential" + " value  list: " +  zkOper.getChildrenKeys("/EphemeralSequential"));
		
	}

}
