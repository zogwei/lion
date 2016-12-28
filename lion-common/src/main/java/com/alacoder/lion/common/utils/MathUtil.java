/*
 *  Copyright 2009-2016 Weibo, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.alacoder.lion.common.utils;


import com.alacoder.common.log.LogFactory;
import com.alacoder.common.log.LogService;

/**
 * @author fishermen
 * @version V1.0 created at: 2013-6-20
 */

public class MathUtil {
	
	private final static LogService log = LogFactory.getLogService(MathUtil.class);

    public static int parseInt(String intStr, int defaultValue) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
        	log.debug("ParseInt false, for malformed intStr:" + intStr);
            return defaultValue;
        }
    }
}
