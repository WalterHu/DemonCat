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
package com.demoncat.dcapp.mvp.impl;

import com.demoncat.dcapp.mvp.BasePresenter;
import com.demoncat.dcapp.mvp.IView;
import com.demoncat.dcapp.network.protocol.LoginManager;
import com.demoncat.dcapp.network.protocol.LoginResult;

/**
 * @Class: LoginPresenter
 * @Description: Login presenter in mvp of login procedure
 *               LoginManager is the M part of mvp
 * @Author: hubohua
 * @CreateDate: 2018/4/13
 */
public class LoginPresenter extends BasePresenter<LoginPresenter.LoginView> {
    private LoginManager loginManager;

    public LoginPresenter(LoginPresenter.LoginView view) {
        super(view);
        loginManager = new LoginManager();
    }

    public void login(String username, String password) {
        loginManager.login(username, password, new LoginManager.LoginResponseCallback() {
            @Override
            public void onLoginFailure() {
                if (mView != null) {
                    mView.onLoginFailure();
                }
            }

            @Override
            public void onRequestStart() {
                if (mView != null) {
                    mView.onLogining();
                }
            }

            @Override
            public void onSuccess(LoginResult result) {
                if (mView != null) {
                    mView.onLoginSuccess(result.token);
                }
            }

            @Override
            public void onRequestFailure(int type, String errorCode) {
                if (mView != null) {
                    mView.onLoginFailure();
                }
            }
        });
    }

    public interface LoginView extends IView {
        public void onLoginSuccess(String token);
        public void onLogining();
        public void onLoginFailure();
    }
}
