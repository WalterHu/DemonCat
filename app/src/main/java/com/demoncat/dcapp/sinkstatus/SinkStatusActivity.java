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
package com.demoncat.dcapp.sinkstatus;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.demoncat.dcapp.R;
import com.demoncat.dcapp.utils.CommonUtils;
import com.demoncat.dcapp.widget.FakeStatusBarView;

/**
 * @Class: SinkStatusActivity
 * @Description: Shows how to accomplish sink status bar
 * @Author: hubohua
 * @CreateDate: 2018/4/12
 */
public class SinkStatusActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sink_status);
        // sinkStatusBar1();
        sinkStatusBar2();
    }

    private void sinkStatusBar1() {
        // 在4.4开始，通过设置windowTranslucentStatus来透明话状态栏
        // 或者在style中通过设置"android:windowTranslucentStatus"为true来设置
        // 但是，仅仅设置该属性, 应用标题栏和状态栏重叠了，相当于整个布局上移了StatusBar的高度。
        // 下属方案是直接在content布局部分顶部增加一个占位组件，高度与statusbar高度相同
        // 6.0上也可直接运行
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup rootView =
                (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        int count = rootView.getChildCount();
        //判断是否已经添加了statusBarView
        if (count > 0 && rootView.getChildAt(0) instanceof FakeStatusBarView) {
            rootView.getChildAt(0).setBackgroundColor(calculateStatusBarColor());
        } else {
            FakeStatusBarView statusBarView = createFakeStatusBar();
            rootView.addView(statusBarView, 0);
        }
//        }
    }

    private void sinkStatusBar2() {
        // 在4.4开始，通过设置windowTranslucentStatus来透明话状态栏
        // 或者在style中通过设置"android:windowTranslucentStatus"为true来设置
        // 但是，仅仅设置该属性, 应用标题栏和状态栏重叠了，相当于整个布局上移了StatusBar的高度。
        // 下述方案是直接在DecorView布局部分最后增加一个占位组件，高度与statusbar高度相同
        // 6.0上也可直接运行
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //获取windowphone下的decorView
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        int count = decorView.getChildCount();
        //判断是否已经添加了statusBarView
        if (count > 0 && decorView.getChildAt(count - 1) instanceof FakeStatusBarView) {
            decorView.getChildAt(count - 1).setBackgroundColor(calculateStatusBarColor());
        } else {
            //新建一个和状态栏高宽的view
            FakeStatusBarView statusView = createFakeStatusBar();
            decorView.addView(statusView);
        }
        ViewGroup rootView =
                (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        // rootview不会为状态栏留出状态栏空间，设置后为状态栏留出空间, 假状态栏就预留出来
        ViewCompat.setFitsSystemWindows(rootView,true);
        rootView.setClipToPadding(true);
//        }
    }

    private FakeStatusBarView createFakeStatusBar() {
        FakeStatusBarView fakeStatusBarView = new FakeStatusBarView(this);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        CommonUtils.getStatusBarHeight(getApplicationContext()));
        fakeStatusBarView.setLayoutParams(params);
        fakeStatusBarView.setBackgroundColor(calculateStatusBarColor());
        return fakeStatusBarView;
    }

    private int calculateStatusBarColor() {
        return getColor(android.R.color.black);
    }
}
