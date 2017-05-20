/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote-api
 * @Title: Response.java
 * @Package com.alacoder.lion.remote.transport
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月10日 下午5:46:19
 * @version V1.0
 */

package com.alacoder.lion.remote.transport;

import java.util.Map;

import com.alacoder.lion.remote.TransportData;

/**
 * @ClassName: Response
 * @Description: 
 * @author jimmy.zhong
 * @date 2017年1月10日 下午5:46:19
 *
 */

public interface Response<T> extends TransportData {
	
	public Long getRequestId();
	
	public void setRequestId(Long requestId);
	
	  /**
     * <pre>
	 * 		如果 request 正常处理，那么会返回 Object value，而如果 request 处理有异常，那么 getValue 会抛出异常
	 * </pre>
     * 
     * @throws RuntimeException
     * @return
     */
    T getValue();
    
//    void setValue(T t);

    /**
     * 如果request处理有异常，那么调用该方法return exception 如果request还没处理完或者request处理正常，那么return null
     * 
     * <pre>
	 * 		该方法不会阻塞，无论该request是处理中还是处理完成
	 * </pre>
     * 
     * @return
     */
    Exception getException();
    
//    void setException(Exception e);

    /**
     * 业务处理时间
     * 
     * @return
     */
    long getProcessTime();

    void setProcessTime(long time);
    
    long getCreateTime();

    int getTimeout();
    
    Map<String, String> getAttachments();

    void setAttachment(String key, String value);
}
