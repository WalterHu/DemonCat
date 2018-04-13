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

import android.app.ProgressDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.demoncat.dcapp.R;
import com.demoncat.dcapp.mvp.BasePresenter;
import com.demoncat.dcapp.mvp.BaseView;

/**
 * @Class: LoginActivity
 * @Description: Login activity for login procedure
 * @Author: hubohua
 * @CreateDate: 2018/4/13
 */
public class LoginActivity extends BaseView implements LoginPresenter.LoginView{
    private LoginPresenter mPresenter = new LoginPresenter(this);
    private ProgressDialog mProgressDialog = null;

    @Override
    protected BasePresenter[] getPresenter() {
        return new BasePresenter[]{mPresenter};
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrname =
                        ((EditText) findViewById(R.id.et_name)).getText().toString();
                String pwd =
                        ((EditText) findViewById(R.id.et_pwd)).getText().toString();
                mPresenter.login(usrname, pwd);
            }
        });
    }

    @Override
    protected void initData() {
    }

    /*-----------Implements of LoginView-----------*/
    @Override
    public void onLoginSuccess(String token) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        Toast.makeText(
                getApplicationContext(), "登录成功 token:" + token, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogining() {
        mProgressDialog =
                ProgressDialog.show(this, null, "登录中...");
    }

    @Override
    public void onLoginFailure() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        Toast.makeText(
                getApplicationContext(), "登录失败!", Toast.LENGTH_SHORT).show();
    }
}
