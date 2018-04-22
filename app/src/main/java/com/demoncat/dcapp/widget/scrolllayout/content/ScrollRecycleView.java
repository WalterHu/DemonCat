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
package com.demoncat.dcapp.widget.scrolllayout.content;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import com.demoncat.dcapp.widget.scrolllayout.ScrollLayout;

/**
 * @Class: ScrollRecycleView
 * @Description: Scroll recycle view for ScrollLayout
 * @Author: hubohua
 * @CreateDate: 2018/4/20
 */
public class ScrollRecycleView extends RecyclerView {
    public ScrollRecycleView(Context context) {
        super(context);
    }

    public ScrollRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = this.getParent();
        while (parent != null) {
            if (parent instanceof ScrollLayout) {
                ((ScrollLayout) parent).setAssociateRecycleViewScrollListener(this);
                break;
            }
            parent = parent.getParent();
        }
    }
}
