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
package com.demoncat.dcapp.widget.swipetoload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.demoncat.dcapp.common.Constants;

/**
 * @Class: LoadMoreFooterView
 * @Description: Customize the footer view of SwipeToLoadLayout
 * @Author: hubohua
 * @CreateDate: 2018/4/20
 */
@SuppressLint("AppCompatCustomView")
public class LoadMoreFooterView extends TextView implements SwipeTrigger, SwipeLoadMoreTrigger {

    private boolean mIsNoMore = false;

    public LoadMoreFooterView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        // Nothing to do
    }

    /**
     * Set no more data status
     * @param noMore
     */
    public void setNoMore(boolean noMore) {
        mIsNoMore = noMore;
    }

    /**
     * Is no more data status
     * @return
     */
    public boolean isNoMore() {
        return mIsNoMore;
    }

    /**
     * Load more callback
     */
    @Override
    public void onLoadMore() {
        Log.d(Constants.TAG_DEMONCAT, "onLoadMore");
        if (mIsNoMore) {
            setText("没有更多了");
        } else {
            setText("正在刷新");
        }
    }

    /**
     * First pull up time
     */
    @Override
    public void onPrepare() {
        Log.d(Constants.TAG_DEMONCAT, "onPrepare");
        if (mIsNoMore) {
            setText("没有更多了");
        } else {
            setText("上拉刷新");
        }
    }

    /**
     * Move state callback
     * @param yScrolled
     * @param isComplete
     * @param automatic
     */
    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        Log.d(Constants.TAG_DEMONCAT, "onMove isComplete: " + isComplete);
        if (mIsNoMore) {
            setText("没有更多了");
        } else {
            if (!isComplete) {
                if (yScrolled <= -getHeight()) {
                    setText("松开刷新");
                }
            }
        }
    }

    /**
     * User release the footer view
     */
    @Override
    public void onRelease() {
        Log.d(Constants.TAG_DEMONCAT, "onRelease");
        if (mIsNoMore) {
            setText("没有更多了");
        }
    }

    /**
     * Load complete.
     * After called setLoadingMore(false)
     */
    @Override
    public void onComplete() {
        Log.d(Constants.TAG_DEMONCAT, "onComplete");
        if (mIsNoMore) {
            setText("没有更多了");
        } else {
            setText("刷新完成");
        }
    }

    /**
     * All load process finished callback
     */
    @Override
    public void onReset() {
        Log.d(Constants.TAG_DEMONCAT, "onReset");
    }
}
