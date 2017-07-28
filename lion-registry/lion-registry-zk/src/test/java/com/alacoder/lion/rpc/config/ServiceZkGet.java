package com.alacoder.lion.rpc.config;

import java.util.List;

import com.alacoder.lion.rpc.registry.zk.CuratorOper;
import com.alacoder.lion.rpc.registry.zk.ZkConfiguration;

public class ServiceZkGet {

	public static void main(String[] args) {
		String registerStr = "zookeeper://127.0.0.1:2181";
		ZkConfiguration zkconfig = getZkConfiguration(registerStr);
		CuratorOper zkOper = new CuratorOper(zkconfig);
		List<String> groups = zkOper.getChildrenKeys("/");
		for(String group : groups){
			System.out.println("group : " +"/"+group);
			List<String> paths = zkOper.getChildrenKeys("/"+group);
			for(String path : paths){
				System.out.println("   path : " + "/lion"+"/"+group+"/"+path+"/service");
				if(zkOper.isExisted("/"+group+"/"+path+"/service")){
					List<String> services = zkOper.getChildrenKeys("/"+group+"/"+path+"/service");
					for(String service : services){
						System.out.println("     service : " + "/lion"+"/"+group+"/"+path+"/service"+"/"+service);
					}
				}
			}
		}
	}
	
	private static ZkConfiguration getZkConfiguration(String registerStr){
		ZkConfiguration zkconf = null;
		String regProtcol = null;
		String ip = null;
		String port = null;
		
		regProtcol = registerStr.substring(0, registerStr.indexOf("//")-1);
		String ipAndPort = registerStr.substring(registerStr.indexOf("//")+2, registerStr.length());
		if(regProtcol.equals("zookeeper")){
			zkconf = new ZkConfiguration(ipAndPort,"lion",1000,1000,3);
		}

		return zkconf;
	}

}
