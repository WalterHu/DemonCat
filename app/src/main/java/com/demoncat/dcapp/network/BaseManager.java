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

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Class: BaseManager
 * @Description: Base manager for http request definition
 *               according to retrofit structure.
 *               Provide the sub manager running environment in rx.
 * @Author: hubohua
 * @CreateDate: 2018/4/13
 */
public abstract class BaseManager {

    /**
     * Request executor by default error transformer and result handle policy.
     * @param observable
     * @param callback
     * @param <T>
     */
    public final <T extends BaseResult> void execute(Observable<T> observable,
                                                     ResponseCallback<T> callback) {
        execute1(observable, new ResultHandler<T>(), callback);
    }

    /**
     * Execute the request by custom ResultHandler
     * @param observable
     * @param resultHandler
     * @param callback
     * @param <T>
     */
    public final <T extends BaseResult> void execute1(Observable<T> observable,
                                                      ResultHandler<T> resultHandler,
                                                      ResponseCallback<T> callback) {
        execute2(observable, new ResultHandler<T>(), new ErrorTransformer<T>(), callback);
    }

    /**
     * Execute request by custom ResultHandler and ErrorTransformer
     * @param observable
     * @param resultHandler
     * @param transformer
     * @param callback
     * @param <T>
     */
    public final <T extends BaseResult> void execute2(Observable<T> observable,
                                                      ResultHandler<T> resultHandler,
                                                      ErrorTransformer<T> transformer,
                                                      ResponseCallback<T> callback) {
        if (observable == null) {
            return;
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnNext(resultHandler)
                .onErrorResumeNext(transformer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    /**
     * Execute the request for Type R as final result type.
     * @param observable
     * @param transformer
     * @param callback
     * @param <T> Original result type returned by http request according to restful protocol
     * @param <R> Final result type returned to up layer in application
     */
    public final <T extends BaseResult, R> void executeTransfom(Observable<T> observable,
                                                                DataTransformer<T, R> transformer,
                                                                ResponseCallback<R> callback) {
        executeTransfom1(observable, new ResultHandler<T>(), transformer, callback);
    }

    /**
     * Execute the request for Type R as final result type.
     * @param observable
     * @param resultHandler
     * @param transformer
     * @param callback
     * @param <T> Original result type returned by http request according to restful protocol
     * @param <R> Final result type returned to up layer in application
     */
    public final <T extends BaseResult, R> void executeTransfom1(Observable<T> observable,
                                                                ResultHandler<T> resultHandler,
                                                                DataTransformer<T, R> transformer,
                                                                ResponseCallback<R> callback) {
        executeTransfom2(observable, resultHandler, transformer, new ErrorTransformer<R>(), callback);
    }

    /**
     * Execute the request for Type R as final result type.
     * @param observable
     * @param resultHandler
     * @param transformer
     * @param errorTransformer
     * @param callback
     * @param <T> Original result type returned by http request according to restful protocol
     * @param <R> Final result type returned to up layer in application
     */
    public final <T extends BaseResult, R> void executeTransfom2(Observable<T> observable,
                                                                 ResultHandler<T> resultHandler,
                                                                 DataTransformer<T, R> transformer,
                                                                 ErrorTransformer<R> errorTransformer,
                                                                 ResponseCallback<R> callback) {
        if (observable == null) {
            return;
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnNext(resultHandler)
                .flatMap(transformer)
                .onErrorResumeNext(errorTransformer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    /**
     * Result handler for result json or exception
     */
    public class ResultHandler<T extends BaseResult> implements Action1<T> {

        /**
         * Implemented by sub class for self handle result.
         * Used to do some fake data monitor
         * @param data represents the result data
         * @return
         */
        public boolean handled(T data) {
            return false;
        }

        @Override
        public final void call(T data) {
            if (data != null) {
                // if handle the data by sub class itself
                // this will not throw error even result code is not success
                if (handled(data)) {
                    return;
                }
                // TODO Pre-handle the result code
                // TODO If result code is not 200 or custom success code
                // TODO Throw a error or exception to go to onError() of ResponseCallback
                if (!"0200".equals(data.resultCode)) {
                    // Wrapper throwable and throw
                    throw new ServerException(data.resultCode);
                }
            }
        }
    }

    /**
     * Handling the data transforming from result type to custom type.
     * Using the flatmap in Rx
     * @param <T>
     * @param <R>
     */
    public abstract class DataTransformer<T extends BaseResult, R> implements Func1<T, Observable<R>> {

        @Override
        public final Observable<R> call(T data) {
            return transformData(data);
        }

        public abstract Observable<R> transformData(T data);
    }

    /**
     * Transform the common exception to corresponding Observable ResponseThrowable
     * @param <T>
     */
    public class ErrorTransformer<T> implements Func1<Throwable, Observable<T>> {

        @Override
        public Observable<T> call(Throwable throwable) {
            // Wrapper the exception before result go to onError()
            // Or you could handle the data to fake data for success
            return Observable.error(ExceptionHandler.handleException(throwable));
        }
    }
}
