/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-server
 * @Title: DefaultAPIServer.java
 * @Package com.alacoder.lion.monitor.op
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月8日 上午10:56:16
 * @version V1.0
 */

package com.alacoder.lion.monitor.op.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpNettyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast("codec", new HttpServerCodec());
        p.addLast("handler", new NettyServerHandler());
    }
}
