/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractProtocol.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午11:20:12
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import java.util.concurrent.ConcurrentHashMap;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.rpc.utils.LionFrameworkUtil;

/**
 * @ClassName: AbstractProtocol
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月22日 上午11:20:12
 *
 */

public abstract class AbstractProtocol implements Protocol {
	
	private final static LogService logger = LogFactory.getLogService(AbstractProtocol.class);
	
	protected ConcurrentHashMap<String, Exporter<?>> exporterMap = new ConcurrentHashMap<String, Exporter<?>>();

	@Override
	public <T> Exporter<T> export(Provider<T> provider, LionURL url) {
		if(url == null ) {
			throw new LionFrameworkException(this.getClass().getSimpleName() + " export Error: url is null",
					LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
		}
		if(provider == null ) {
			 throw new LionFrameworkException(this.getClass().getSimpleName() + " export Error: provider is null, url=" + url,
					 LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
		}
		String protocolKey = LionFrameworkUtil.getProtocolKey(url);
		
		synchronized(exporterMap) {
			@SuppressWarnings("unchecked")
			Exporter<T> exporter = (Exporter<T>)exporterMap.get(protocolKey);
			if( exporter != null) {
				logger.warn(" export Error: service already exist, url= " + url);
				return exporter;
			}
			exporter = createExporter(provider,url);
			exporter.init();
			exporterMap.put(protocolKey, exporter);
			 
   		    logger.info(this.getClass().getSimpleName() + " export Success: url=" + url);

	       return exporter;
		}
		
	}
	
	protected abstract <T> Exporter<T> createExporter(Provider<T> provider, LionURL url);

    @Override
    public <T> Referer<T> refer(Class<T> clz, LionURL url, LionURL serviceUrl) {
        if (url == null) {
            throw new LionFrameworkException(this.getClass().getSimpleName() + " refer Error: url is null",
                    LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
        }

        if (clz == null) {
            throw new LionFrameworkException(this.getClass().getSimpleName() + " refer Error: class is null, url=" + url,
                    LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
        }

        Referer<T> referer = createReferer(clz, url, serviceUrl);
        referer.init();

        logger.info(this.getClass().getSimpleName() + " refer Success: url=" + url);

        return referer;
    }


    protected abstract <T> Referer<T> createReferer(Class<T> clz, LionURL url, LionURL serviceUrl);

    @Override
    public void destroy() {
        for (String key : exporterMap.keySet()) {
            Node node = exporterMap.remove(key);

            if (node != null) {
                try {
                    node.destroy();

                    logger.info(this.getClass().getSimpleName() + " destroy node Success: " + node);
                } catch (Throwable t) {
                    logger.error(this.getClass().getSimpleName() + " destroy Error", t);
                }
            }
        }
    }

}
