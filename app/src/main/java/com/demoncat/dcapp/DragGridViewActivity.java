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
    private List<Data> mData;

    private int mCurrPage = -1; // default drag gridview page
    private int mNumPerPage = DEFAULT_PAGE_COLUM * DEFAULT_PAGE_ROWS;

    private static final int DEFAULT_PAGE_COLUM = 4;
    private static final int DEFAULT_PAGE_ROWS = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);
        mDragPager = findViewById(R.id.dg_pager);
        mDragPager.setColCount(DEFAULT_PAGE_COLUM);
        mDragPager.setRowCount(DEFAULT_PAGE_ROWS);
        mAdapter = new DragViewAdapter(getApplicationContext());
        mDragPager.setAdapter(mAdapter);
        mData = getData();
        mAdapter.setData(mData);
        mDragPager.setOnRearrangeListener(new DragGridViewPager.OnRearrangeListener() {
            @Override
            public void onRearrange(int oldIndex, int newIndex) {
                mAdapter.swipeData(oldIndex, newIndex);
            }

            @Override
            public void onFinished(int index) {
                // when switch index has changed
                finishEditMode();
                setToCorrectPage(index);
            }
        });

        mDragPager.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mData.get(position) != null && !mData.get(position).isSlot()) {
                    // should return true to handle the long click event
                    appendNewEmtyPage();
                    return true;
                }
                return false;
            }
        });
    }

    private List<Data> getData() {
        List<Data> data = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            data.add(new Data(false, "Item-" + i));
        }
        appendLeftEmptySlot(data);
        return data;
    }

    /**
     * Append the empty slot for the page which item count is not full

     */
    private void appendLeftEmptySlot(List<Data> data) {
        if (data != null && !data.isEmpty()) {
            // calculate left empty slot at last page
            int left = mNumPerPage - data.size() % mNumPerPage;
            if (left > 0 && left < mNumPerPage) {
                for (int i = 0; i < left; i ++) {
                    Data d = new Data(true, "empty");
                    data.add(d); // full the left index item
                }
            }
        }
    }

    /**
     * Append new empty slot page for edit mode
     */
    private void appendNewEmtyPage() {
        if (mData != null) {
            List<Data> emptySlot = new ArrayList<>();
            for (int i = 0; i < mNumPerPage; i ++) {
                Data d = new Data(true, "empty");
                emptySlot.add(d); // add empty slot for size 8
            }
            mAdapter.setEditMode(true);
            mAdapter.addData(emptySlot); // mData could be add new empty slot of size per page
        }
    }

    /**
     * Finish edit mode and recalculate the page and empty slot
     */
    private void finishEditMode() {
        if (mData != null && !mData.isEmpty()) {
            int size = mData.size();
            int index = -1; // record the last not empty item index
            for (int i = size - 1; i > -1; i --) {
                if (mData.get(i) != null && !mData.get(i).isSlot()) {
                    // find the last not empty item
                    index = i;
                    break;
                }
            }
            if (index <= -1 || index == size - 1) {
                // find none or the index is last one
                // which means the page has no left empty slot
                // nothing to do
            } else if (index + 1 >= size - 1) {
                // the last second one, remove the last one
                mData.remove(size - 1);
            } else {
                // remove the left slot from the last not empty item
                mData.subList(index + 1, size).clear();
            }
            appendLeftEmptySlot(mData);
            mAdapter.setEditMode(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Set to current targe index of page
     * When item moves to side of current page and tap up,
     * the item would return to the original index slot.
     * But the pager will stay at the last empty new page
     * which should be delete when sliding end.
     * @param index final index
     */
    private void setToCorrectPage(int index) {
        // calculate current page
        if (mData != null && !mData.isEmpty()) {
            if (index >= 0 && index < mData.size()) {
                int pageIndex = index / mNumPerPage;
                Log.d(Constants.TAG_DEMONCAT,
                        "OnRearrangeListener.onFinished pageIndex: " + pageIndex);
                if (pageIndex >= 0) {
                    mDragPager.setCurrentItem(pageIndex);
                }
            }
        }
    }

    private static class DragViewAdapter extends BaseAdapter {
        private List<Data> mData;
        private boolean mEditMode;
        private Context mContext;

        public DragViewAdapter(Context context) {
            mContext = context;
        }

        /**
         *
         * @param data
         */
        public void setData(List<Data> data) {
            mData = data;
            notifyDataSetChanged();
        }

        /**
         * Add more data list to current content
         * @param data
         */
        public void addData(List<Data> data) {
            if (mData == null) {
                mData = new ArrayList<>();
            }
            mData.addAll(data);
            notifyDataSetChanged();
        }

        /**
         * Switch item position in index1 and index2
         * @param index1
         * @param index2
         */
        public void swipeData(int index1, int index2) {
            if (index1 >= 0 && index1 < getCount()
                    && index2 >= 0 && index2 < getCount()) {
                Data d1 = mData.get(index1);
                Data d2 = mData.get(index2);
                mData.set(index1, d2);
                mData.set(index2, d1);
                notifyDataSetChanged();
            }
        }

        /**
         * Set in edit mode
         * When edit mode the page would add another empty one
         * @param editMode
         */
        public void setEditMode(boolean editMode) {
            mEditMode = editMode;
        }

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_drag_grid_view, null);
                viewHolder = new ViewHolder();
                viewHolder.name = convertView.findViewById(R.id.item_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (mData != null && mData.size() > position) {
                viewHolder.name.setText(mData.get(position).getName());
            }
            return convertView;
        }

        private static class ViewHolder {
            public TextView name;
        }
    }

    private static class Data {
        private boolean isSlot; // whether is an empty data
        private String name;

        public Data(boolean isSlot, String name) {
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
