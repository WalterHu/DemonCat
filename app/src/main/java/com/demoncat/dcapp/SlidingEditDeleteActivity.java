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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.demoncat.dcapp.widget.slidebutton.SlidingEditAdapter;
import com.demoncat.dcapp.widget.slidebutton.SlidingEditHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Class: SlidingEditDeleteActivity
 * @Description: Sliding for edit/delete menu list view activity
 * @Author: hubohua
 * @CreateDate: 2018/4/18
 */
public class SlidingEditDeleteActivity extends Activity {
    private EditDeleteAdapter mAdapter;
    private RecyclerView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_edit);
        mListView = findViewById(R.id.sliding_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(manager);
        mAdapter = new EditDeleteAdapter(this);
        mAdapter.setItemClickListener(new SlidingEditAdapter.OnSlidingViewClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                Toast.makeText(getApplicationContext(),
                        "点击了第" + (position + 1) + "项", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteBtnClick(View view, int position, Object data) {
                Toast.makeText(getApplicationContext(),
                        "删除了第" + (position + 1) + "项", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEditBtnClick(View view, int position, Object data) {
                Toast.makeText(getApplicationContext(),
                        "编辑了第" + (position + 1) + "项", Toast.LENGTH_SHORT).show();
            }
        });
        mListView.setAdapter(mAdapter);
        mAdapter.updateData(getData());
    }

    private List<Data> getData() {
        List<Data> data = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            Data data1 = new Data("我是第" + (i + 1) + "项数据");
            data.add(data1);
        }
        return data;
    }

    private static class Data {
        private String name;

        public Data(String name) {
            this.name = name;
        }
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
}
