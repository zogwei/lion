/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: ClusterSupport.java
 * @Package com.alacoder.lion.rpc.ha
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午5:03:18
 * @version V1.0
 */

package com.alacoder.lion.rpc.ha;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.alacoder.lion.registry.NotifyListener;
import com.alacoder.lion.registry.Registry;
import com.alacoder.lion.registry.RegistryFactory;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.log.logging.Log;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.extension.ExtensionLoader;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.common.url.URLParamType;
import com.alacoder.lion.common.utils.CollectionUtil;
import com.alacoder.lion.common.utils.StringTools;
import com.alacoder.lion.rpc.Protocol;
import com.alacoder.lion.rpc.Referer;
import com.alacoder.lion.rpc.filter.ProtocolFilterDecorator;

/**
 * @ClassName: ClusterSupport
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月9日 下午5:03:18
 *
 */

public class ClusterSupport<T> implements NotifyListener {

	private final static Log logger = LogFactory.getLog(ClusterSupport.class);
	
	private static ConcurrentHashMap<String, Protocol> protocols = new ConcurrentHashMap<String, Protocol>();
	private Cluster<T> cluster;
	private List<LionURL> registryUrls;
	private LionURL url;
	private Class<T> interfaceClass;
    private Protocol protocol;
    //一个点阅的服务，要到多个注册中心去找服务器提供者，每一个注册中心下面有多个服务提供者，一个服务提供者可以注册到多个注册中心
    private ConcurrentHashMap<LionURL, List<Referer<T>>> registryReferers = new ConcurrentHashMap<LionURL, List<Referer<T>>>();
	
	public ClusterSupport(Class<T> interfaceClass, List<LionURL> registryUrls) {
		this.registryUrls = registryUrls;
		this.interfaceClass = interfaceClass;
		String urlStr = StringTools.urlDecode(registryUrls.get(0).getParameter(URLParamType.embed.getName()));
		this.url = LionURL.valueOf(urlStr); 
		protocol = getDecorateProtocol(url.getProtocol());
	}
	
	public void init() {
		prepareCluster();
		
		 LionURL subUrl = toSubscribeUrl(url);
		 for (LionURL ru : registryUrls) {
			 String directUrlStr = ru.getParameter(URLParamType.directUrl.getName());
			// 如果有directUrl，直接使用这些directUrls进行初始化，不用到注册中心discover
			if (StringUtils.isNotBlank(directUrlStr)) {
				List<LionURL> directUrls = parseDirectUrls(directUrlStr);
				if (!directUrls.isEmpty()) {
					notify(ru, directUrls);
					logger.info("Use direct urls, refUrl={}, directUrls={}", url,directUrls);
					continue;
				}
			}
			
			 // client 注册自己，同时订阅service列表
            Registry registry = getRegistry(ru);
            //注册自己
            registry.register(toConsumerUrl(url));
            //订阅
            registry.subscribe(subUrl, this);
            
		 }
		
		 boolean check = Boolean.parseBoolean(url.getParameter(URLParamType.check.getName(), URLParamType.check.getValue()));
	     if (!CollectionUtil.isEmpty(cluster.getReferers()) || !check) {
			 cluster.init();
			 if (CollectionUtil.isEmpty(cluster.getReferers()) && !check) {
				logger.warn(String.format("refer:%s", this.url.getPath() + "/" + this.url.getVersion()), "No services");
			 }
			 return;
	     }
	     else{
		        throw new LionFrameworkException(String.format("ClusterSupport No service urls for the refer:%s, registries:%s",
		                this.url.getIdentity(), registryUrls), LionErrorMsgConstant.SERVICE_UNFOUND);
	     }
	}
	
    protected Registry getRegistry(LionURL url) {
        RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(url.getProtocol());
        return registryFactory.getRegistry(url);
    }
	
    private List<LionURL> parseDirectUrls(String directUrlStr) {
        String[] durlArr = LionConstants.COMMA_SPLIT_PATTERN.split(directUrlStr);
        List<LionURL> directUrls = new ArrayList<LionURL>();
        for (String dus : durlArr) {
            LionURL du = LionURL.valueOf(StringTools.urlDecode(dus));
            if (du != null) {
                directUrls.add(du);
            }
        }
        return directUrls;
    }
	 
    @SuppressWarnings("unchecked")
	private void prepareCluster() {
        String clusterName = url.getParameter(URLParamType.cluster.getName(), URLParamType.cluster.getValue());
        String loadbalanceName = url.getParameter(URLParamType.loadbalance.getName(), URLParamType.loadbalance.getValue());
        String haStrategyName = url.getParameter(URLParamType.haStrategy.getName(), URLParamType.haStrategy.getValue());
        
        cluster = ExtensionLoader.getExtensionLoader(Cluster.class).getExtension(clusterName);
        LoadBalance<T> loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(loadbalanceName);
        HaStrategy<T> ha = ExtensionLoader.getExtensionLoader(HaStrategy.class).getExtension(haStrategyName);
        
        cluster.setLoadBalance(loadBalance);
        cluster.setHaStrategy(ha);
        cluster.setUrl(url);
	}
	
	private LionURL toSubscribeUrl(LionURL url) {
		LionURL subUrl = url.createCopy();
		subUrl.addParameter(URLParamType.nodeType.getName(), LionConstants.NODE_TYPE_SERVICE);
		return subUrl;
	}
	
	private LionURL toConsumerUrl(LionURL url) {
		LionURL subUrl = url.createCopy();
		subUrl.addParameter(URLParamType.nodeType.getName(), LionConstants.NODE_TYPE_REFERER);
		return subUrl;
	}
	
	protected Protocol getDecorateProtocol(String protocolName) {
        Protocol decorateProtocol = protocols.get(protocolName);
        if (decorateProtocol == null) {
            protocols.putIfAbsent(protocolName, new ProtocolFilterDecorator(ExtensionLoader.getExtensionLoader(Protocol.class)
                    .getExtension(protocolName)));
            decorateProtocol = protocols.get(protocolName);
        }
        return decorateProtocol;
    }

    /**
     * <pre>
     * 1 notify的执行需要串行
     * 2 notify通知都是全量通知，在设入新的referer后，cluster需要把不再使用的referer进行回收，避免资源泄漏;
     * 3 如果该registry对应的referer数量为0，而没有其他可用的referers，那就忽略该次通知；
     * 4 此处对protoco进行decorator处理，当前为增加filters
     * </pre>
     */
	@Override
	public synchronized void notify(LionURL registryUrl, List<LionURL> urls) {
		// TODO Auto-generated method stub
		if(CollectionUtil.isEmpty(registryUrls)) {
			onRegistryEmpty(registryUrl);
            logger.warn("ClusterSupport config change notify, urls is empty: registry={} service={} urls=[]", registryUrl.getUri(), url.getIdentity());

            return;
		}
		
		 logger.info("ClusterSupport config change notify: registry={} service={} urls={}", registryUrl.getUri(), url.getIdentity(), getIdentities(urls));
		 
	     // 通知都是全量通知，在设入新的referer后，cluster内部需要把不再使用的referer进行回收，避免资源泄漏
         //////////////////////////////////////////////////////////////////////////////////
		 
		 // 判断urls中是否包含权重信息，并通知loadbalance。
	     processWeights(urls);
	     
	     List<Referer<T>> newReferers = new ArrayList<Referer<T>>();
	     for(LionURL u : urls) {
	    	 if(!u.canServe(url)) {
	    		 continue;
	    	 }
	    	 Referer<T> referer  = getExistingReferer(u,registryReferers.get(registryUrl));
	    	 if(referer == null) {
	    		 LionURL refererURL = u.createCopy();
	    		 mergeClientConfigs(refererURL);
	    		 referer = protocol.refer(interfaceClass, refererURL, u);
	    	 }
	    	 if (referer != null) {
	             newReferers.add(referer);
	         }
	     }
	     
	     if(CollectionUtil.isEmpty(newReferers)) {
	    	  onRegistryEmpty(registryUrl);
	          return;
	     }
	     
	     // 此处不销毁referers，由cluster进行销毁
	     registryReferers.put(registryUrl, newReferers);
	     refreshCluster();    

	}
	
	 /**
     * refererURL的扩展参数中，除了application、module外，其他参数被client覆盖， 如果client没有则使用referer的参数
     *
     * @param refererURL
     */
    private void mergeClientConfigs(LionURL refererURL) {
        String application = refererURL.getParameter(URLParamType.application.getName(), URLParamType.application.getValue());
        String module = refererURL.getParameter(URLParamType.module.getName(), URLParamType.module.getValue());
        refererURL.addParameters(this.url.getParameters());

        refererURL.addParameter(URLParamType.application.getName(), application);
        refererURL.addParameter(URLParamType.module.getName(), module);
    }
	
    private Referer<T> getExistingReferer(LionURL url, List<Referer<T>> referers) {
    	if(referers == null) {
    		return null;
    	}
    	for(Referer<T> r : referers) {
    		if(ObjectUtils.equals(url, r.getUrl()) || ObjectUtils.equals(url, r.getServiceUrl())){
    			return r;
    		}
    	}
    	
    	return null;
    }
	
	/**
     * 检查urls中的第一个url是否为权重信息。 如果是权重信息则把权重信息传递给loadbalance，并移除权重url。
     *  TODO 1、第一个权重信息什么时候设置的，2、多个注册中间，权重如何控制
     * @param urls
     */
    private void processWeights(List<LionURL> urls) {
        if (urls != null && !urls.isEmpty()) {
            LionURL ruleUrl = urls.get(0);
            // 没有权重时需要传递默认值。因为可能是变更时去掉了权重
            String weights = URLParamType.weights.getValue();
            if ("rule".equalsIgnoreCase(ruleUrl.getProtocol())) {
                weights = ruleUrl.getParameter(URLParamType.weights.getName(), URLParamType.weights.getValue());
                urls.remove(0);
            }
            logger.info("refresh weight. weight=" + weights);
            this.cluster.getLoadBalance().setWeightString(weights);
        }
    }
	

    private String getIdentities(List<LionURL> urls) {
        if (urls == null || urls.isEmpty()) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (LionURL u : urls) {
            builder.append(u.getIdentity()).append(",");
        }
        builder.setLength(builder.length() - 1);
        builder.append("]");

        return builder.toString();
    }
	
	private void onRegistryEmpty(LionURL excludeRegistryUrl) {
		boolean noMoreOtherRefers = registryReferers.size() == 1 && registryReferers.contains(excludeRegistryUrl);
		if(noMoreOtherRefers) {
			logger.warn(String.format("Ignore notify for no more referers in this cluster, registry: %s, cluster=%s", excludeRegistryUrl, getUrl()));
		}
		else {
			registryReferers.remove(excludeRegistryUrl);
			refreshCluster();
		}
	}
	
    private void refreshCluster() {
        List<Referer<T>> referers = new ArrayList<Referer<T>>();
        for (List<Referer<T>> refs : registryReferers.values()) {
            referers.addAll(refs);
        }
        cluster.onRefresh(referers);
    }
    public LionURL getUrl() {
        return url;
    }
    public Cluster<T> getCluster() {
        return cluster;
    }

    
    public void destroy() {
        LionURL subscribeUrl = toSubscribeUrl(url);
        for (LionURL ru : registryUrls) {
            try {
                Registry registry = getRegistry(ru);
                registry.unsubscribe(subscribeUrl, this);
                if (!LionConstants.NODE_TYPE_REFERER.equals(url.getParameter(URLParamType.nodeType.getName()))) {
                    registry.unregister(url);
                }
            } catch (Exception e) {
                logger.warn(String.format("Unregister or unsubscribe false for url (%s), registry= %s", url, ru.getIdentity()), e);
            }

        }
        try {
            getCluster().destroy();
        } catch (Exception e) {
            logger.warn(String.format("Exception when destroy cluster: %s", getCluster().getUrl()));
        }
    }
}
