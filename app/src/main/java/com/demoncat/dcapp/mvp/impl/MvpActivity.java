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

import android.view.View;

import com.demoncat.dcapp.R;
import com.demoncat.dcapp.mvp.BasePresenter;
import com.demoncat.dcapp.mvp.BaseView;

/**
 * @Class: MvpActivity
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/4/12
 */
public class MvpActivity extends BaseView implements MvpPresenter.MvpView {
    private MvpPresenter mPresenter = new MvpPresenter(this);

    @Override
    protected BasePresenter[] getPresenter() {
        return new BasePresenter[]{mPresenter};
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mvp;
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.action1();
                }
            }
        });
    }

    @Override
    protected void initData() {
        // do some data initialize
    }


}
