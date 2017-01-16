/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AccessLogFilter.java
 * @Package com.alacoder.lion.filter
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午6:30:00
 * @version V1.0
 */

package com.alacoder.lion.rpc.filter;

import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.extension.Activation;
import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.NetUtils;
import com.alacoder.lion.common.utils.StringTools;
import com.alacoder.lion.rpc.Caller;
import com.alacoder.lion.rpc.Filter;
import com.alacoder.lion.rpc.Provider;
import com.alacoder.lion.rpc.remote.RpcRequest;
import com.alacoder.lion.rpc.remote.RpcRequestInfo;
import com.alacoder.lion.rpc.remote.RpcResponse;

/**
 * @ClassName: AccessLogFilter
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午6:30:00
 *
 */

@SpiMeta(name = "access")
@Activation(sequence = 100)
public class AccessLogFilter implements Filter {

	private final static LogService logger = LogFactory.getLogService(AccessLogFilter.class);
	
    private String side;

    @Override
    public RpcResponse filter(Caller<?> caller, RpcRequest request) {
        boolean needLog = caller.getUrl().getBooleanParameter(URLParamType.accessLog.getName(), URLParamType.accessLog.getBooleanValue());
        if (needLog) {
            long t1 = System.currentTimeMillis();
            boolean success = false;
            try {
                RpcResponse response = caller.call(request);
                success = true;
                return response;
            } finally {
                long consumeTime = System.currentTimeMillis() - t1;
                logAccess(caller, request, consumeTime, success);
            }
        } else {
            return caller.call(request);
        }
    }

    private void logAccess(Caller<?> caller, RpcRequest request, long consumeTime, boolean success) {
        if (getSide() == null) {
            String side = caller instanceof Provider ? LionConstants.NODE_TYPE_SERVICE : LionConstants.NODE_TYPE_REFERER;
            setSide(side);
        }
        
    	RpcRequestInfo rpcRequestInfo =  request.getRequestMsg();

        StringBuilder builder = new StringBuilder(128);
        append(builder, side);
        append(builder, caller.getUrl().getParameter(URLParamType.application.getName()));
        append(builder, caller.getUrl().getParameter(URLParamType.module.getName()));
        append(builder, NetUtils.getLocalAddress().getHostAddress());
        append(builder, rpcRequestInfo.getInterfaceName());
        append(builder, rpcRequestInfo.getMethodName());
        append(builder, rpcRequestInfo.getParamtersDesc());
        // 对于client，url中的remote ip, application, module,referer 和 service获取的地方不同
        if (LionConstants.NODE_TYPE_REFERER.equals(side)) {
            append(builder, caller.getUrl().getHost());
            append(builder, caller.getUrl().getParameter(URLParamType.application.getName()));
            append(builder, caller.getUrl().getParameter(URLParamType.module.getName()));
        } else {
            append(builder, rpcRequestInfo.getAttachments().get(URLParamType.host.getName()));
            append(builder, rpcRequestInfo.getAttachments().get(URLParamType.application.getName()));
            append(builder, rpcRequestInfo.getAttachments().get(URLParamType.module.getName()));
        }

        append(builder, success);
        append(builder, rpcRequestInfo.getAttachments().get(URLParamType.requestIdFromClient.getName()));
        append(builder, consumeTime);

        logger.accessLog(builder.substring(0, builder.length() - 1));
    }

    private void append(StringBuilder builder, Object field) {
        if (field != null) {
            builder.append(StringTools.urlEncode(field.toString()));
        }
        builder.append(LionConstants.SEPERATOR_ACCESS_LOG);
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }


}
