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

package com.alacoder.common.exception;


/**
 * @author maijunsheng
 * @version 创建时间：2013-5-30
 * 
 */
public abstract class LionAbstractException extends RuntimeException {
    private static final long serialVersionUID = -8742311167276890503L;

    protected LionErrorMsg motanErrorMsg = LionMsgConstant.FRAMEWORK_DEFAULT_ERROR;
    protected String errorMsg = null;

    public LionAbstractException() {
        super();
    }

    public LionAbstractException(LionErrorMsg motanErrorMsg) {
        super();
        this.motanErrorMsg = motanErrorMsg;
    }

    public LionAbstractException(String message) {
        super(message);
        this.errorMsg = message;
    }

    public LionAbstractException(String message, LionErrorMsg motanErrorMsg) {
        super(message);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public LionAbstractException(String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = message;
    }

    public LionAbstractException(String message, Throwable cause, LionErrorMsg motanErrorMsg) {
        super(message, cause);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public LionAbstractException(Throwable cause) {
        super(cause);
    }

    public LionAbstractException(Throwable cause, LionErrorMsg motanErrorMsg) {
        super(cause);
        this.motanErrorMsg = motanErrorMsg;
    }

    @Override
    public String getMessage() {
        if (motanErrorMsg == null) {
            return super.getMessage();
        }

        String message;

        if (errorMsg != null && !"".equals(errorMsg)) {
            message = errorMsg;
        } else {
            message = motanErrorMsg.getMessage();
        }

        // TODO 统一上下文 requestid
        return "error_message: " + message + ", status: " + motanErrorMsg.getStatus() + ", error_code: " + motanErrorMsg.getErrorCode()
                + ",r=";
    }

    public int getStatus() {
        return motanErrorMsg != null ? motanErrorMsg.getStatus() : 0;
    }

    public int getErrorCode() {
        return motanErrorMsg != null ? motanErrorMsg.getErrorCode() : 0;
    }

    public LionErrorMsg getMotanErrorMsg() {
        return motanErrorMsg;
    }
}
