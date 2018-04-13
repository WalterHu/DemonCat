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
package com.demoncat.dcapp.network.protocol;

import com.demoncat.dcapp.network.BaseManager;
import com.demoncat.dcapp.network.ExceptionHandler;
import com.demoncat.dcapp.network.ResponseCallback;
import com.demoncat.dcapp.network.ServiceFactory;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @Class: LoginManager
 * @Description: Login request manager
 * @Author: hubohua
 * @CreateDate: 2018/4/13
 */
public class LoginManager extends BaseManager {

    /**
     * Login service interface for retrofit with rx
     */
    interface LoginService {
        @POST("api/login")
        Observable<LoginResult> login(@Body LoginParams params); // restful type request using json
    }

    /**
     * Login request
     * @param username
     * @param password
     * @param callback
     */
    public void login(String username, String password, LoginResponseCallback callback) {
        if (callback != null) {
            // 1. Create Login service
            LoginService service = ServiceFactory.getInstance().create(LoginService.class);
            // 2. Create Login params
            LoginParams params = new LoginParams(username, password);
            // 3. Create Observable object by calling service function
            Observable<LoginResult> observable = service.login(params);
            // 4. Execute the request
            // execute(observable, callback);
            // execute1(observable, new LoginDataHandler(), callback);
            execute2(observable, new LoginDataHandler(), new LoginErrorTransformer(), callback);
        }
    }

    /**
     * Login response result handler
     */
    public abstract static class LoginResponseCallback extends ResponseCallback<LoginResult> {
        public abstract void onLoginFailure();

        @Override
        protected boolean filterCustomError(Throwable throwable) {
            // TODO Do some handling to throwable yourself
            if (throwable instanceof ExceptionHandler.ResponseThrowable) {
                ExceptionHandler.ResponseThrowable serverException
                        = (ExceptionHandler.ResponseThrowable) throwable;
                if (ExceptionHandler.ERROR.SERVER_ERROR == serverException.type) {
                    // The result will lead to onLoginFailure function for caller
                    onLoginFailure();
                    return true;
                }
            }
            // Other exception will lead to the common response handler
            return false;
        }
    }

    // login result data handler for fake login success data
    private class LoginDataHandler extends ResultHandler<LoginResult> {
        @Override
        public boolean handled(LoginResult data) {
            if (data == null) {
                data = new LoginResult();
                data.resultCode = "0200";
                data.token = "1234567890";
            }
            return true;
        }
    }

    /**
     * Transform the exception to fake login result object.
     * Lead the failure result to success one.
     */
    private class LoginErrorTransformer extends ErrorTransformer<LoginResult> {
        @Override
        public Observable<LoginResult> call(Throwable throwable) {
            LoginResult data = new LoginResult();
            data.resultCode = "0200";
            data.token = "1234567890";
            return Observable.just(data);
        }
    }
}
