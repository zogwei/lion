/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-netty
 * @Title: NettyChannelHandler.java
 * @Package com.alacoder.lion.remote.netty
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月12日 下午1:48:35
 * @version V1.0
 */

package com.alacoder.lion.remote.netty;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.alacoder.lion.common.utils.LoggerUtil;
import com.alacoder.lion.remote.Channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @ClassName: NettyChannelHandler
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月12日 下午1:48:35
 *
 */

public class NettyServerChannelHandler extends ChannelInboundHandlerAdapter {
	
	private int MaxChannelNum = 0;
	private ConcurrentMap<String, Channel> channels = null;
	
	public NettyServerChannelHandler(int MaxChannelNum, ConcurrentMap<String, Channel> channels){
		super();
		this.MaxChannelNum = MaxChannelNum;
		this.channels = channels;
	}

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	LoggerUtil.info("server chanel channelRead ");
    	NioSocketChannel nioChannel = (NioSocketChannel) msg;
//    	
//    	String channelKey = getChannelKey((InetSocketAddress) nioChannel.localAddress(), (InetSocketAddress) nioChannel.remoteAddress());
//
//		if (channels.size() > MaxChannelNum) {
//			// 超过最大连接数限制，直接close连接
//			LoggerUtil.warn("NettyServerChannelManage channelConnected channel size out of limit: limit={} current={}",
//					MaxChannelNum, channels.size());
//
//			nioChannel.close();
//		} else {
//			channels.put(channelKey, nioChannel);
			ctx.fireChannelRead(msg);
//		}
    }
    
    private void removeChannel(ChannelHandlerContext ctx){
    	io.netty.channel.Channel channel = ctx.channel();
    	String channelKey = getChannelKey((InetSocketAddress) channel.localAddress(), (InetSocketAddress) channel.remoteAddress());

		channels.remove(channelKey);
		LoggerUtil.debug("server chanel remove  " + channelKey);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	LoggerUtil.debug("server chanel inactive ");
        ctx.fireChannelInactive();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
    	LoggerUtil.warn("server chanel exceptionCaught ");
        ctx.fireExceptionCaught(cause);
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }
    
	/**
	 * remote address + local address 作为连接的唯一标示
	 * 
	 * @param local
	 * @param remote
	 * @return
	 */
	private String getChannelKey(InetSocketAddress local, InetSocketAddress remote) {
		String key = "";
		if (local == null || local.getAddress() == null) {
			key += "null-";
		} else {
			key += local.getAddress().getHostAddress() + ":" + local.getPort() + "-";
		}

		if (remote == null || remote.getAddress() == null) {
			key += "null";
		} else {
			key += remote.getAddress().getHostAddress() + ":" + remote.getPort();
		}

		return key;
	}
	
	public Map<String, Channel> getChannels() {
		return channels;
	}
	
	/**
	 * close所有的连接
	 */
	public void close() {
		for (Map.Entry<String, Channel> entry : channels.entrySet()) {
			try {
				Channel channel = entry.getValue();
				
				if (channel != null) {
					LoggerUtil.info("chanel inactive " + channel.toString());
					channel.close();
				}
			} catch (Exception e) {
				LoggerUtil.error("NettyServerChannelManage close channel Error: " + entry.getKey(), e);
			}
		}
	}

}
