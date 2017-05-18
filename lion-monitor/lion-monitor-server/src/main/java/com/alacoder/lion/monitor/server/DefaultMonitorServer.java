/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-monitor-api
 * @Title: DefaultMonitorServer.java
 * @Package com.alacoder.lion.monitor.api
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月7日 下午4:22:21
 * @version V1.0
 */

package com.alacoder.lion.monitor.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.utils.BeanUtils;
import com.alacoder.lion.monitor.DefaultMonitorMsgHandler;
import com.alacoder.lion.monitor.DefaultRpcMonitorMsg;
import com.alacoder.lion.monitor.MonitorMsg;
import com.alacoder.lion.monitor.MonitorMsgHandler;
import com.alacoder.lion.remote.Channel;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.Server;
import com.alacoder.lion.remote.TransportData;
import com.alacoder.lion.remote.netty.NettyServer;
import com.alacoder.lion.remote.transport.DefaultRequest;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.WorkerPool;

/**
 * @ClassName: DefaultMonitorServer
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年2月7日 下午4:22:21
 *
 */

public class DefaultMonitorServer implements MonitorServer{

	private final static Log logger = LogFactory.getLog(DefaultMonitorServer.class);

	@SuppressWarnings("unused")
	private final static Integer MAX_QUEUE= 10000;
	
	private MonitorMsgHandler handler = null;
	private Server remoteServer = null;
	
	RingBuffer<Request<MonitorMsg>> ringBuffer = null;
	int BUFFER_SIZE= 1024;
	int THREAD_NUMBERS= 4;
	
			
	public DefaultMonitorServer(LionURL lionURL){
		this(lionURL,null);
	}
	
	@SuppressWarnings("unchecked")
	public DefaultMonitorServer(LionURL lionURL,MonitorMsgHandler handler){
		if(handler == null){
			this.handler = new DefaultMonitorMsgHandler();
		}
		else {
			this.handler = handler;
		}
		remoteServer = new NettyServer(lionURL, new MonitorMessageHandler());
		remoteServer.open();
		
		EventFactory<Request<MonitorMsg>> eventFactory=new EventFactory<Request<MonitorMsg>>() {
			public Request<MonitorMsg> newInstance() {
				return new DefaultRequest<MonitorMsg>();
			}
		};
		ringBuffer=RingBuffer.createSingleProducer(eventFactory, BUFFER_SIZE);
		SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBERS);
		
		DisputorMsgHandler disputorMsgHandler = new DisputorMsgHandler(this.handler);
		WorkHandler<Request<MonitorMsg>> workHandlers= disputorMsgHandler;
		WorkerPool<Request<MonitorMsg>> workerPool=new WorkerPool<Request<MonitorMsg>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), workHandlers);
		workerPool.start(executor);
	}
	
	class MonitorMessageHandler implements MessageHandler{

		@Override
		public Response<?> handle(Channel channel, Request<?> request) {
			Object msg = request.getRequestMsg();
			if(msg instanceof DefaultRpcMonitorMsg) {
				logger.info(msg.toString());
				long seq=ringBuffer.next();
				Request<MonitorMsg> temp = ringBuffer.get(seq);
				BeanUtils.copyProperties(temp,request);
				ringBuffer.publish(seq);
			}
			else {
				logger.error("no know type , msg : " + msg.toString());
			}
			
			DefaultResponse<String> ret = new DefaultResponse<String>();
			ret.setRequestId(request.getId());
			
			return ret;
		}

		@Override
		public Object handle(Channel channel, Response<?> response) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object handle(Channel channel, TransportData response) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	
}
