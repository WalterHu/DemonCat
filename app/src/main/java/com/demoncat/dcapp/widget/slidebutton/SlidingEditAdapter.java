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
package com.demoncat.dcapp.widget.slidebutton;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demoncat.dcapp.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Class: SlidingEditAdapter
 * @Description: Sliding edit & delete adapter
 * @Author: hubohua
 * @CreateDate: 2018/4/17
 */
public abstract class SlidingEditAdapter<E, T extends SlidingEditHolder> extends RecyclerView.Adapter<T>
        implements SlidingDeleteView.OnSlidingStateChangeListener<SlidingEditView> {

    protected Context mContext;
    protected List<E> mContents = new ArrayList<>();

    private SlidingDeleteView mMenu = null;
    private SlidingEditAdapter.OnSlidingViewClickListener mIClickBtnClickListener;

    public SlidingEditAdapter(Context context) {
        mContext = context;
    }

    /**
     * Set the item click listener
     * @param listener
     */
    public void setItemClickListener(SlidingEditAdapter.OnSlidingViewClickListener listener) {
        mIClickBtnClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mContents.size();
    }

    @Override
    public void onBindViewHolder(final T holder, int position) {
        // Force set the layout content width to screen width size
        holder.layoutContent.getLayoutParams().width = CommonUtils.getScreenWidth(mContext);
        holder.layoutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the menu open state
                if (menuIsOpen()) {
                    closeMenu(); // 关闭菜单
                } else {
                    int n = holder.getLayoutPosition();
                    if (mIClickBtnClickListener != null) {
                        mIClickBtnClickListener.onItemClick(v, n, mContents.get(n));
                    }
                }
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();
                if (mIClickBtnClickListener != null) {
                    mIClickBtnClickListener.onDeleteBtnClick(v, n, mContents.get(n));
                }
                if (menuIsOpen()) {
                    closeMenu(); // 关闭菜单
                }
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();
                if (mIClickBtnClickListener != null) {
                    mIClickBtnClickListener.onEditBtnClick(v, n, mContents.get(n));
                }
                // Close the menu when click happens
                if (menuIsOpen()) {
                    closeMenu(); // 关闭菜单
                }
            }
        });
        bindHolder(holder, position);
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int type) {
        View view = LayoutInflater.from(mContext)
                .inflate(getItemContentRes(), parent, false);
        return createHolder(view);
    }

    /**
     * Update data
     * @param data
     */
    public void updateData(List<E> data) {
        mContents = data;
        notifyDataSetChanged();
    }

    /**
     * Add data
     * @param position
     */
    public void addData(int position, E data) {
        mContents.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * Remove data
     * @param position
     */
    public void removeData(int position) {
        mContents.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Update single data of position
     * @param position
     */
    public void updateSingleData(E data, int position) {
        mContents.set(position, data);
        notifyItemChanged(position);
    }

    /**
     * Menu open
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingEditView) view;
    }

    /**
     * On down or move sliding button happened
     *
     * @param
     */
    @Override
    public void onDownOrMove(SlidingEditView slidingEditView) {
        if (menuIsOpen()) {
            if (mMenu != slidingEditView) {
                closeMenu();
            }
        }
    }

    /**
     * Close menu
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;
    }

    public void recycle() {
        if (mContents != null) {
            mContents.clear();
        }
        mContents = null;
        mContext = null;
        mIClickBtnClickListener = null;
    }

    /**
     * Whether menu is open
     */
    public Boolean menuIsOpen() {
        return mMenu != null;
    }

    /*------ Sub class implement ------*/
    protected abstract int getItemContentRes();
    protected abstract T createHolder(View itemView);
    protected abstract void bindHolder(final T holder, int position);

    /**
     * Sliding button view click or delete click listener
     * T1 equals to E type
     */
    public interface OnSlidingViewClickListener<T1> {
        void onItemClick(View view, int position, T1 data);
        void onDeleteBtnClick(View view, int position, T1 data);
        void onEditBtnClick(View view, int position, T1 data);
    }
}
