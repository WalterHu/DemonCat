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
package com.demoncat.dcapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

/**
 * @Class: SwipeToLoadActivity
 * @Description: Swipe to load activity
 * @Author: hubohua
 * @CreateDate: 2018/4/20
 */
public class SwipeToLoadActivity extends Activity {
    private TextView mTvContent;
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_to_load);
        mTvContent = findViewById(R.id.tv_swipe_content);
        mSwipeToLoadLayout = findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // when refresh happens, you could do some work to load data.
                requestForData(true);
            }
        });
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // when refresh happens, you could do some work to load data.
                requestForData(false);
            }
        });
    }

    private void requestForData(final boolean refresh) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // stop refreshing or loading more
                if (!refresh) {
                    mSwipeToLoadLayout.setLoadingMore(false);
                    mTvContent.setText("上拉加载内容");
                } else {
                    mSwipeToLoadLayout.setRefreshing(false);
                    mTvContent.setText("下拉加载内容");
                }
            }
        }, 3000l);
    }
}
