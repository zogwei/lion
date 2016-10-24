package com.alacoder.lion.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alacoder.common.exception.LionServiceException;
import com.alacoder.lion.common.LionConstants;
/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: ConfigUtil.java
 * @Package 
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 上午9:50:13
 * @version V1.0
 */
import com.alacoder.lion.common.utils.MathUtil;

/**
 * @ClassName: ConfigUtil
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月20日 上午9:50:13
 *
 */

public class ConfigUtil {

    /**
     * export fomart: protocol1:port1,protocol2:port2
     * 
     * @param export
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Integer> parseExport(String export) {
        if (StringUtils.isBlank(export)) {
            return Collections.emptyMap();
        }
        Map<String, Integer> pps = new HashMap<String, Integer>();
        String[] protocolAndPorts = LionConstants.COMMA_SPLIT_PATTERN.split(export);
        for (String pp : protocolAndPorts) {
            if (StringUtils.isBlank(pp)) {
                continue;
            }
            String[] ppDetail = pp.split(":");
            if (ppDetail.length == 2) {
                pps.put(ppDetail[0], Integer.parseInt(ppDetail[1]));
            } else if (ppDetail.length == 1) {
                if(LionConstants.PROTOCOL_INJVM.equals(ppDetail[0])){
                    pps.put(ppDetail[0], LionConstants.DEFAULT_INT_VALUE);
                } else{
                    int port = MathUtil.parseInt(ppDetail[0], 0);
                    if(port <= 0){
                        throw new LionServiceException("Export is malformed :" + export);
                    }
                }
                
            } else {
                throw new LionServiceException("Export is malformed :" + export);
            }
        }
        return pps;
    }

    public static String extractProtocols(String export) {
        Map<String, Integer> protocols = parseExport(export);
        StringBuilder sb = new StringBuilder(16);
        for (String p : protocols.keySet()) {
            sb.append(p).append(LionConstants.COMMA_SEPARATOR);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();

    }
}
