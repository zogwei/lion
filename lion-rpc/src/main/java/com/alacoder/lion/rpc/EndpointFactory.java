/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: EndpointFactory.java
 * @Package com.alacoder.lion.rpc
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月23日 上午10:36:40
 * @version V1.0
 */

package com.alacoder.lion.rpc;

import com.alacoder.lion.common.extension.Scope;
import com.alacoder.lion.common.extension.Spi;
import com.alacoder.lion.common.url.LionURL;
import com.alacoder.lion.remote.Client;
import com.alacoder.lion.remote.MessageHandler;
import com.alacoder.lion.remote.Server;

/**
 * @ClassName: EndpointFactory
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年9月23日 上午10:36:40
 *
 */
@Spi(scope = Scope.SINGLETON)
public interface EndpointFactory {

    Server createServer(LionURL url, MessageHandler messageHandler);
    
    Client createClient(LionURL url, MessageHandler messageHandler);
    
    void safeReleaseResource(Server server, LionURL url);
    
    void safeReleaseResource(Client client, LionURL url);
}
