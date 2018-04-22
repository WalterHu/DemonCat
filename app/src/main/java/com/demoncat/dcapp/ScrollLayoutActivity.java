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
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.demoncat.dcapp.common.Constants;
import com.demoncat.dcapp.utils.CommonUtils;
import com.demoncat.dcapp.widget.scrolllayout.FullyLinearLayoutManager;
import com.demoncat.dcapp.widget.scrolllayout.ScrollLayout;
import com.demoncat.dcapp.widget.scrolllayout.content.ScrollRecycleView;
import com.demoncat.dcapp.widget.slidebutton.SlidingEditAdapter;
import com.demoncat.dcapp.widget.slidebutton.SlidingEditHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Class: ScrollLayoutActivity
 * @Description: ScrollLayout activity
 * @Author: hubohua
 * @CreateDate: 2018/4/20
 */
public class ScrollLayoutActivity extends Activity {
    private ScrollRecycleView mRecycleView;
    private ScrollLayout mScrollLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_layout);
        mRecycleView = findViewById(R.id.scroll_recycle);
        // mRecycleView.setHasFixedSize(true);
        // FullLinearLayoutManager fix the bug for ScrollLayout with RecycleView when scrolling
        mRecycleView.setLayoutManager(new FullyLinearLayoutManager(this));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        // mRecycleView.setNestedScrollingEnabled(false);
        mRecycleView.setAdapter(new EditDeleteAdapter(this));
        // init scroll layout
        // ScrollLayout在设置时，需要将其minOffset设置为0，即最大升起高度与屏幕的差值，其表示的状态为
        // 拉起ScrollLayout到最大的状态下距离其组件top的差值。这里需要设置为0。
        // 即不能够让ScrollLayout为了留出其父布局的顶部而设置minOffset，而是通过设置ScrollLayout的外部Margin
        // 来实现，并且保证高minoffset为0.
        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
        mScrollLayout.setMaxOffset(CommonUtils.dip2px(getApplicationContext(), 336));
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setExitOffset(CommonUtils.dip2px(getApplicationContext(), 168));
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setToOpen();
        mScrollLayout.setAssociateRecycleViewScrollListener(mRecycleView);
        mScrollLayout.setOnScrollChangedListener(new ScrollLayout.OnScrollChangedListener() {
            @Override
            public void onScrollProgressChanged(float currentProgress) {
            }

            @Override
            public void onScrollFinished(ScrollLayout.Status currentStatus) {
                Log.d(Constants.TAG_DEMONCAT, "onScrollFinished state: " + currentStatus);
            }

            @Override
            public void onChildScroll(int top) {
            }
        });
        // init data
        List<Data> data = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {
            data.add(new Data("Item " + (i + 1)));
        }
        ((EditDeleteAdapter) mRecycleView.getAdapter()).updateData(data);
    }

    private static class EditDeleteAdapter extends SlidingEditAdapter<Data, EditDeleteHolder> {

        public EditDeleteAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemContentRes() {
            return R.layout.item_sliding_edit_delete;
        }

        @Override
        protected EditDeleteHolder createHolder(View itemView) {
            return new EditDeleteHolder(itemView, this);
        }

        @Override
        protected void bindHolder(EditDeleteHolder holder, int position) {
            if (holder != null && mContents != null && !mContents.isEmpty()) {
                if (mContents.size() > position) {
                    Data data = mContents.get(position);
                    holder.mTvContent.setText(data.name);
                }
            }
        }
    }

    /**
     * Custom sliding edit and delete holder
     */
    private static class EditDeleteHolder extends SlidingEditHolder<EditDeleteAdapter> {
        private TextView mTvContent;

        public EditDeleteHolder(View itemView, EditDeleteAdapter adapter) {
            super(itemView, adapter);
        }

        @Override
        public void initViews(View itemView) {
            if (itemView != null) {
                mTvContent = itemView.findViewById(R.id.tv_content);
            }
        }

        @Override
        public int getSlidingViewId() {
            // return the sliding edit view id
            return R.id.sliding_layout;
        }
    }

    private static class Data {
        private String name;

        public Data(String name) {
            this.name = name;
        }
    }
}
