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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demoncat.dcapp.common.Constants;
import com.demoncat.dcapp.widget.DragGridViewApdater;
import com.demoncat.dcapp.widget.DragGridViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @Class: DragGridViewActivity
 * @Description: Drag grid view activity
 * @Author: hubohua
 * @CreateDate: 2018/4/17
 */
public class DragGridViewActivity extends Activity {
    private DragGridViewPager mDragPager;
    private DragViewAdapter mAdapter;
    private TextView mTvPager;

    private int mCurrPage = -1; // default drag gridview page
    private int mNumPerPage = DEFAULT_PAGE_COLUM * DEFAULT_PAGE_ROWS;

    private static final int DEFAULT_PAGE_COLUM = 4;
    private static final int DEFAULT_PAGE_ROWS = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);
        mDragPager = findViewById(R.id.dg_pager);
        mAdapter = new DragViewAdapter(getApplicationContext());
        mAdapter.attach(mDragPager);
        mAdapter.setData(getData());
    }

    private List<MyData> getData() {
        List<MyData> data = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            data.add(new MyData(false, "Item-" + i));
        }
        return data;
    }

    private static class DragViewAdapter extends DragGridViewApdater {

        public DragViewAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getCountCols() {
            return DEFAULT_PAGE_COLUM;
        }

        @Override
        protected int getCountRows() {
            return DEFAULT_PAGE_ROWS;
        }

        @Override
        protected Data createEmptyItem() {
            return new MyData(true, "empty");
        }

        @Override
        protected int getContentLayout() {
            return R.layout.item_drag_grid_view;
        }

        @Override
        protected void bindDataHolder(View convertView, Data data, int position, boolean isSlot) {
            if (convertView != null && data != null) {
                ViewHolder holder = (ViewHolder) convertView.getTag();
                if (holder == null) {
                    holder = new ViewHolder();
                    holder.name = convertView.findViewById(R.id.item_name);
                    convertView.setTag(holder);
                }
                // check the data slot empty or not
                if (data.isSlot()) {
                    holder.name.setText("empty item");
                } else {
                    holder.name.setText(((MyData) data).getName());
                }
            }
        }

        private static class ViewHolder {
            public TextView name;
        }
    }

    private static class MyData implements DragGridViewApdater.Data {
        private boolean isSlot; // whether is an empty data
        private String name;

        public MyData(boolean isSlot, String name) {
            this.isSlot = isSlot;
            this.name = name;
        }

        public boolean isSlot() {
            return isSlot;
        }

        public void setSlot(boolean slot) {
            isSlot = slot;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
