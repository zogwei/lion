/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: DefaultProvider.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午4:56:22
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.lang.reflect.Method;

import com.alacoder.common.exception.LionBizException;
import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionServiceException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.remote.transport.DefaultResponse;
import com.alacoder.lion.remote.transport.Request;
import com.alacoder.lion.remote.transport.Response;

/**
 * @ClassName: DefaultProvider
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月21日 下午4:56:22
 *
 */
@SpiMeta(name = "lion")
public class DefaultProvider<T> extends AbstractProvider<T> {
	
	private final static LogService logger = LogFactory.getLogService(DefaultProvider.class);

	protected T proxyImpl;

	public DefaultProvider(T proxyImpl, LionURL url, Class<T> clz) {
		super(url, clz);
		this.proxyImpl = proxyImpl;
	}

	@Override
	protected Response invoke(Request request) {
		DefaultResponse response = new DefaultResponse();
		
		Method method = lookup(request);
		if(method == null) {
			LionServiceException exception = new LionServiceException("Service method not exist: " + request.getInterfaceName() + "." + request.getMethodName()
                    + "(" + request.getParamtersDesc() + ")", LionErrorMsgConstant.SERVICE_UNFOUND);
			response.setException(exception);
			return response;
		}
		
		try{
			Object value = method.invoke(proxyImpl, request.getArguments());
			response.setValue(value);
		}
		catch(Exception e){
			if (e.getCause() != null) {
                logger.error("Exception caught when method invoke: " + e.getCause());
                response.setException(new LionBizException("provider call process error", e.getCause()));
            } else {
                response.setException(new LionBizException("provider call process error", e));
            }
		}
		catch(Throwable t){
			// 如果服务发生Error，将Error转化为Exception，防止拖垮调用方
            if (t.getCause() != null) {
                response.setException(new LionBizException("provider has encountered a fatal error!", t.getCause()));
            } else {
                response.setException(new LionBizException("provider has encountered a fatal error!", t));
            }
		}
		
        // 传递rpc版本和attachment信息方便不同rpc版本的codec使用。
        response.setRpcProtocolVersion(request.getRpcProtocolVersion());
        response.setAttachments(request.getAttachments());
        return response;
	}
}
