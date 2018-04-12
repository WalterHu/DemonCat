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
package com.demoncat.dcapp.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.demoncat.dcapp.common.Constants;

import java.lang.ref.WeakReference;

/**
 * @Class: BaseView
 * @Description: Base view defines the View of MVP
 * @Author: hubohua
 * @CreateDate: 2018/4/12
 */
public abstract class BaseView extends Activity implements IView {
    private BasePresenter[] mAllPresenters = null;

    protected Handler mHandler = new MyHandler(this);

    protected abstract BasePresenter[] getPresenter();
    protected abstract int getLayoutId(); // get layout resource id
    protected abstract void initView(); // init view contents
    protected abstract void initData(); // do some data initialize

    private class MyHandler extends Handler {
        private WeakReference<BaseView> mHost;

        public MyHandler(BaseView host) {
            super();
            mHost = new WeakReference<BaseView>(host);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init all presenter will be used
        mAllPresenters = getPresenter();
        setContentView(getLayoutId());
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        // destroy all presenters when destroy view
        if (mAllPresenters != null && mAllPresenters.length > 0) {
            for (BasePresenter presenter : mAllPresenters) {
                presenter.destroy();
            }
        }
    }

    @Override
    public void onActionStart() {
        Log.d(Constants.TAG_DEMONCAT, getClass().getSimpleName() + " -> " + "onActionStart");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Action starts...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActionSuccess() {
        Log.d(Constants.TAG_DEMONCAT, getClass().getSimpleName() + " -> " + "onActionSuccess");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Action success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActionFailure() {
        Log.d(Constants.TAG_DEMONCAT, getClass().getSimpleName() + " -> " + "onActionFailure");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Action failure!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
