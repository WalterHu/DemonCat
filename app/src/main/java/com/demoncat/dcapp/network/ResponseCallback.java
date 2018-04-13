/**
 * Copyright 2018 hubohua
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demoncat.dcapp.network;

import rx.Subscriber;

/**
 * @Class: ResponseCallback
 * @Description: Common response callback of subscriber type
 * @Author: hubohua
 * @CreateDate: 2018/4/13
 */
public abstract class ResponseCallback<T> extends Subscriber<T> {

    public abstract void onRequestStart();
    public abstract void onSuccess(T result);
    public abstract void onRequestFailure(int type, String errorCode);

    /**
     * Default none handling of custom error handled by sub callback
     * They could filter their own error code or type
     * @param throwable
     * @return
     */
    protected boolean filterCustomError(Throwable throwable) {
        return false;
    }

    @Override
    public final void onStart() {
        super.onStart();
        onRequestStart();
    }

    @Override
    public final void onCompleted() {
    }

    @Override
    public final void onError(Throwable throwable) {
        // filter the common error should be handled uniformly
        if (!filterCustomError(throwable)) {
            // handle the common handling
            if (throwable instanceof ExceptionHandler.ResponseThrowable) {
                ExceptionHandler.ResponseThrowable e =
                        (ExceptionHandler.ResponseThrowable) throwable;
                onRequestFailure(e.type, e.code);
            } else {
                onRequestFailure(-1, null); // unknown filtered error
            }
        }
    }

    @Override
    public final void onNext(T result) {
        onSuccess(result);
    }
}
