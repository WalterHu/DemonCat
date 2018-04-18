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
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.demoncat.dcapp.R;
import com.demoncat.dcapp.utils.CommonUtils;

/**
 * @Class: SlidingEditView
 * @Description: Sliding edit & delete menu view
 * @Author: hubohua
 * @CreateDate: 2018/4/18
 */
public class SlidingEditView extends SlidingDeleteView {

    private TextView mTvEdit;

    public SlidingEditView(Context context) {
        super(context);
    }

    public SlidingEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void measures(int widthMeasureSpec, int heightMeasureSpec) {
        if (!mOnce) {
            mTvDelete = (TextView) findViewById(R.id.tv_delete);
            mTvEdit = (TextView) findViewById(R.id.tv_edit);
            mOnce = true;
        }
    }

    @Override
    protected void layout(boolean changed, int l, int t, int r, int b) {
        super.layout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(0, 0);
            mScrollWidth = mTvDelete.getWidth() + mTvEdit.getWidth()
                    + CommonUtils.dip2px(mContext, 1); // 1dp divider
        }
    }

    @Override
    protected void scrollChanged(int l, int t, int oldl, int oldt) {
        super.scrollChanged(l, t, oldl, oldt);
        // mTvDelete.setTranslationX(l - mScrollWidth);
        // mTvEdit.setTranslationX(l - mScrollWidth);
    }

}
