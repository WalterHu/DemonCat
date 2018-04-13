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

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * @Class: ExceptionHandler
 * @Description: Common exception definition wrapper.
 * @Author: hubohua
 * @CreateDate: 2018/4/13
 */
public class ExceptionHandler {

    public static ResponseThrowable handleException(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            return new ResponseThrowable(e, String.valueOf(httpException.code()), ERROR.HTTP_ERROR);
        }  else if (e instanceof ServerException) { // server error
            ServerException resultException = (ServerException) e;
            return new ResponseThrowable(resultException, resultException.code, ERROR.SERVER_ERROR);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            return new ResponseThrowable(e, ERROR.PARSE_ERROR);
        } else if (e instanceof ConnectException) {
            return new ResponseThrowable(e, ERROR.NETWORD_ERROR);
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            return new ResponseThrowable(e, ERROR.SSL_ERROR);
        } else if (e instanceof ConnectTimeoutException){
            return new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
        } else if (e instanceof java.net.SocketTimeoutException) {
            return new ResponseThrowable(e, ERROR.NETWORD_ERROR);
        } else {
            return new ResponseThrowable(e, ERROR.UNKNOWN);
        }
    }

    /**
     * Error type
     */
    public static class ERROR {
        /**
         * Unknown error
         */
        public static final int UNKNOWN = 1000;
        /**
         * Parse error
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * Network error
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * Http error
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * Certificate error
         */
        public static final int SSL_ERROR = 1005;

        /**
         * Timeout error
         */
        public static final int TIMEOUT_ERROR = 1006;

        /**
         * Server Error
         */
        public static final int SERVER_ERROR = 1007;
    }

    public static class ResponseThrowable extends Exception {
        public String code;
        public int type;

        public ResponseThrowable(Throwable throwable, int type) {
            this(throwable, null, type);
        }

        public ResponseThrowable(Throwable throwable, String code, int type) {
            super(throwable);
            this.code = code;
            this.type = type;
        }
    }

}

