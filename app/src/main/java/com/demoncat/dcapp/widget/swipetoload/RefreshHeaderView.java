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

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.demoncat.dcapp.R;
import com.demoncat.dcapp.utils.CommonUtils;

/**
 * @Class: RefreshHeaderView
 * @Description: Customize the header view of SwipeToLoadLayout
 * @Author: hubohua
 * @CreateDate: 2018/4/20
 */
public class RefreshHeaderView extends LinearLayout implements SwipeRefreshTrigger, SwipeTrigger {

    private ImageView mIvArrow;

    private TextView mTvRefresh;

    private int mHeaderHeight;

    private Animation mRotateUp;

    private Animation mRotateDown;

    private boolean mRotated = false;

    public RefreshHeaderView(Context context) {
        this(context, null);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeaderHeight = CommonUtils.dip2px(context, 50); // default 50dp
        mRotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        mRotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTvRefresh = (TextView) findViewById(R.id.tvRefresh);
        mIvArrow = (ImageView) findViewById(R.id.ivArrow);
    }

    @Override
    public void onRefresh() {
        mIvArrow.clearAnimation();
        mIvArrow.setVisibility(GONE);
        mTvRefresh.setText("正在刷新");
    }

    @Override
    public void onPrepare() {
        Log.d("TwitterRefreshHeader", "onPrepare()");
        mIvArrow.clearAnimation();
        mIvArrow.setRotation(180f); // default 180
        mIvArrow.setVisibility(View.VISIBLE);
        mRotated = false;
        mTvRefresh.setText("下拉刷新");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (y > mHeaderHeight) {
                // start the rotate of arrow animation up
                if (!mRotated) {
                    mIvArrow.clearAnimation();
                    mIvArrow.startAnimation(mRotateUp);
                    mRotated = true;
                }
                // if the header moves more than its height
                // it means reach the bottom
                mTvRefresh.setText("松开刷新");
            }
        }
    }

    @Override
    public void onRelease() {
        Log.d("TwitterRefreshHeader", "onRelease()");
    }

    @Override
    public void onComplete() {
        mTvRefresh.setText("刷新完成");
    }

    @Override
    public void onReset() {
    }
}
