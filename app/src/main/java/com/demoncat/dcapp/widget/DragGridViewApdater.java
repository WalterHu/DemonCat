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
package com.demoncat.dcapp.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Class: DragGridViewApdater
 * @Description: DragGridViewPager attachable adapter
 * @Author: hubohua
 * @CreateDate: 2018/4/18
 */
public abstract class DragGridViewApdater<T extends DragGridViewApdater.Data> extends BaseAdapter
        implements DragGridViewPager.OnRearrangeListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = DragGridViewApdater.class.getSimpleName();

    private Context mContext;
    private boolean mEditMode;
    private DragGridViewPager mPagerView;
    private List<T> mData = new ArrayList<>();

    protected abstract int getCountCols();
    protected abstract int getCountRows();
    protected abstract T createEmptyItem();
    protected abstract int getContentLayout();
    protected abstract void bindDataHolder(View convertView, T data, int position, boolean isSlot);

    public DragGridViewApdater(Context context) {
        this.mContext = context;
    }

    /**
     * Attach the pager view with adapter
     * @param pagerView
     */
    public void attach(DragGridViewPager pagerView) {
        if (pagerView != null) {
            mPagerView = pagerView;
            mPagerView.setColCount(getCountCols());
            mPagerView.setRowCount(getCountRows());
            mPagerView.setAdapter(this);
            mPagerView.setOnRearrangeListener(this);
            mPagerView.setOnItemLongClickListener(this);
        }
    }

    /**
     * Set the content of adapter
     * @param data
     */
    public final void setData(List<T> data) {
        mData = data;
        appendLeftEmptySlot(); // append empty slot for new data
        notifyDataSetChanged();
    }

    /**
     * Add more data list to current content
     * @param data
     */
    public final void addData(List<T> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * Append the empty slot for the page which item count is not full

     */
    private void appendLeftEmptySlot() {
        if (mData != null && !mData.isEmpty()) {
            // calculate left empty slot at last page
            int left = getCountRows() * getCountCols() -
                    mData.size() % (getCountRows() * getCountCols());
            if (left > 0 && left < (getCountRows() * getCountCols())) {
                for (int i = 0; i < left; i ++) {
                    T data = createEmptyItem();
                    mData.add(data); // full the left index item
                }
            }
        }
    }

    /**
     * Switch item position in index1 and index2
     * @param index1
     * @param index2
     */
    public final void swipeData(int index1, int index2) {
        if (index1 >= 0 && index1 < getCount()
                && index2 >= 0 && index2 < getCount()) {
            T d1 = mData.get(index1);
            T d2 = mData.get(index2);
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
    public final void setEditMode(boolean editMode) {
        mEditMode = editMode;
    }

    @Override
    public final int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public final T getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public final long getItemId(int position) {
        return mData == null ? 0 : position;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(mContext).inflate(getContentLayout(), null);
        }
        if (mData != null && !mData.isEmpty() && position < mData.size()) {
            T data = mData.get(position);
            if (data != null) {
                // commnon data view
                bindDataHolder(convertView, data, position, data.isSlot());
            }
        }
        return convertView;
    }

    /**
     * Data interface
     * Which has a property to identify the empty slot
     */
    public interface Data {
        public boolean isSlot();
    }

    /*----- Implements of RearrangeListener for DragGridViewPager -----*/

    @Override
    public void onRearrange(int oldIndex, int newIndex) {
        // swipe the data from old index to new index
        swipeData(oldIndex, newIndex);
    }

    @Override
    public void onFinished(int targetIndex) {
        // finish target index
        finishEditMode();
        setToCorrectPage(targetIndex);
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
            appendLeftEmptySlot();
            setEditMode(false);
            notifyDataSetChanged();
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
                int pageIndex = index / (mPagerView.getRowCount() * mPagerView.getColCount());
                Log.d(TAG,
                        "OnRearrangeListener.onFinished pageIndex: " + pageIndex);
                if (pageIndex >= 0) {
                    mPagerView.setCurrentItem(pageIndex);
                }
            }
        }
    }

    /*----- Implements of AdapterView.OnItemLongClickListener -----*/
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mData.get(position) != null && !mData.get(position).isSlot()) {
            // should return true to handle the long click event
            appendNewEmtyPage();
            return true;
        }
        return false;
    }

    /**
     * Append new empty slot page for edit mode
     */
    private void appendNewEmtyPage() {
        if (mData != null) {
            List<T> emptySlot = new ArrayList<>();
            for (int i = 0; i < getCountCols() * getCountRows(); i ++) {
                T data = createEmptyItem();
                emptySlot.add(data); // add empty slot for size 8
            }
            setEditMode(true);
            addData(emptySlot); // mData could be add new empty slot of size per page
        }
    }
}
