/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyServer.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月5日 下午4:54:08
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;

import java.util.Collection;
import java.util.Map;

import com.alacoder.lion.common.url.URL;
import com.alacoder.lion.remote.AbstractServer;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.Codec;
import com.alacoder.lion.remote.Handler;
import com.alacoder.lion.remote.Server;

/**
 * @ClassName: NettyServer
 * @Description: TODO
 * @author jimmy.zhong
 * @date 2016年8月5日 下午4:54:08
 *
 */

public class NettyServer extends AbstractServer{

	private Channel channel;
	private Map<String, Channel> channels;
	private Handler handler ;
	
	public NettyServer(URL url,Handler handler) {
		super(url);
	}
	
	public Codec getCodec(URL url) {
		return codec;
		
		
	}

	/*
	  * <p>Title: getChannels</p>
	  * <p>Description: </p>
	  * @return
	  * @see com.alacoder.lion.remote.Server#getChannels()
	  */
	
	
	@Override
	public Collection<Channel> getChannels() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	  * <p>Title: getServerChannel</p>
	  * <p>Description: </p>
	  * @return
	  * @see com.alacoder.lion.remote.Server#getServerChannel()
	  */
	
	
	@Override
	public Channel getServerChannel() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
