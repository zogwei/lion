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
 * wrapper biz exception.
 * 
 * @author maijunsheng
 * 
 */
public class LionBizException extends LionAbstractException {
    private static final long serialVersionUID = -3491276058323309898L;

    public LionBizException() {
        super(LionMsgConstant.BIZ_DEFAULT_EXCEPTION);
    }

    public LionBizException(LionErrorMsg lionErrorMsg) {
        super(lionErrorMsg);
    }

    public LionBizException(String message) {
        super(message, LionMsgConstant.BIZ_DEFAULT_EXCEPTION);
    }

    public LionBizException(String message, LionErrorMsg lionErrorMsg) {
        super(message, lionErrorMsg);
    }

    public LionBizException(String message, Throwable cause) {
        super(message, cause, LionMsgConstant.BIZ_DEFAULT_EXCEPTION);
    }

    public LionBizException(String message, Throwable cause, LionErrorMsg lionErrorMsg) {
        super(message, cause, lionErrorMsg);
    }

    public LionBizException(Throwable cause) {
        super(cause, LionMsgConstant.BIZ_DEFAULT_EXCEPTION);
    }

    public LionBizException(Throwable cause, LionErrorMsg lionErrorMsg) {
        super(cause, lionErrorMsg);
    }
}
