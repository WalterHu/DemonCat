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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demoncat.dcapp.R;

/**
 * @Class: SlidingEditHolder
 * @Description: Sliding edit delete view holder
 * @Author: hubohua
 * @CreateDate: 2018/4/18
 */
public abstract class SlidingEditHolder<T extends SlidingEditAdapter> extends RecyclerView.ViewHolder {
    public TextView btnEdit; // button edit
    public TextView btnDelete; // button delete
    public ViewGroup layoutContent; // content layout

    public SlidingEditHolder(View itemView, T adapter) {
        super(itemView);
        btnDelete = (TextView) itemView.findViewById(R.id.tv_delete);
        btnEdit = (TextView) itemView.findViewById(R.id.tv_edit);
        layoutContent = (ViewGroup) itemView.findViewById(R.id.layout_content);
        // set the adapter to implement the sliding edit view listener
        ((SlidingEditView) itemView.findViewById(getSlidingViewId())).
                setSlidingStateChangeListener(adapter);
        initViews(itemView);
    }

    public abstract void initViews(View itemView);
    public abstract int getSlidingViewId();
}
