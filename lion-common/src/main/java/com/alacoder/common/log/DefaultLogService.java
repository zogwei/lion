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

package com.alacoder.common.log;

public class DefaultLogService implements LogService {
    private com.aben.cup.log.logging.Log log = null;
    
    public DefaultLogService(com.aben.cup.log.logging.Log log){
    	this.log = log;
    }

    public void trace(String msg) {
    	log.trace(msg);
    }

    @Override
    public void trace(String format, Object... argArray) {
    	log.trace(format, argArray);
    }

    public void debug(String msg) {
    	log.debug(msg);
    }

    public void debug(String format, Object... argArray) {
    	log.debug(format, argArray);
    }

    public void debug(String msg, Throwable t) {
    	log.debug(msg, t);
    }

    public void info(String msg) {
    	log.info(msg);
    }

    public void info(String format, Object... argArray) {
    	log.info(format, argArray);
    }

    public void info(String msg, Throwable t) {
    	log.info(msg, t);
    }

    public void warn(String msg) {
    	log.warn(msg);
    }

    public void warn(String format, Object... argArray) {
    	log.warn(format, argArray);
    }

    public void warn(String msg, Throwable t) {
    	log.warn(msg, t);
    }

    public void error(String msg) {
    	log.error(msg);
    }

    public void error(String format, Object... argArray) {
    	log.error(format, argArray);
    }

    public void error(String msg, Throwable t) {
    	log.error(msg, t);
    }

    public void accessLog(String msg) {
    	log.info(msg);
    }

    public void accessStatsLog(String msg) {
    	log.info(msg);
    }

    public void accessStatsLog(String format, Object... argArray) {
    	log.info(format, argArray);
    }

    public void accessProfileLog(String format, Object... argArray) {
    	log.info(format, argArray);
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public boolean isStatsEnabled() {
        return log.isInfoEnabled();
    }
}
