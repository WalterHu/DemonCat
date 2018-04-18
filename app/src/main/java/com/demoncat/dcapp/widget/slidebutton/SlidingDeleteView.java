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

/**
 * @Class: SlidingDeleteView
 * @Description: Sliding delete menu view
 * @Author: hubohua
 * @CreateDate: 2018/4/17
 */
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.demoncat.dcapp.R;

/**
 * @Class: SlidingDeleteView
 * @Description: Sliding delete view horizontally
 * @Author: hubohua
 * @CreateDate: 2018/4/17
 */
public class SlidingDeleteView extends HorizontalScrollView {
    private static final String TAG = SlidingDeleteView.class.getSimpleName();

    protected Context mContext;
    protected TextView mTvDelete; // delete button
    protected int mScrollWidth;
    protected OnSlidingStateChangeListener mOnSlidingClickListener;
    protected boolean mSlideEnabled = true;

    protected Boolean mOpen = false;
    protected Boolean mOnce = false;

    public SlidingDeleteView(Context context) {
        this(context, null);
    }

    public SlidingDeleteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingDeleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_NEVER);
        mContext = context;
    }

    /**
     * Could slide or not
     * @param slideEnabled
     */
    public void setSlideEnabled(boolean slideEnabled){
        this.mSlideEnabled = slideEnabled;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measures(widthMeasureSpec, heightMeasureSpec);
    }

    protected void measures(int widthMeasureSpec, int heightMeasureSpec) {
        if (!mOnce) {
            mTvDelete = (TextView) findViewById(R.id.tv_delete);
            mOnce = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        layout(changed, l, t, r, b);
    }

    protected void layout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            this.scrollTo(0, 0);
            mScrollWidth = mTvDelete.getWidth();
            Log.i(TAG, "mScrollWidth:" + mScrollWidth);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (mSlideEnabled) { //可以被滑动
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    mOnSlidingClickListener.onDownOrMove(this);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    changeScrollx();
                    return true;
                default:
                    break;
            }
            return super.onTouchEvent(ev);
        } else {
            //不能被滑动
            return true;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        scrollChanged(l, t, oldl, oldt);
    }

    protected void scrollChanged(int l, int t, int oldl, int oldt) {
        // mTvDelete.setTranslationX(l - mScrollWidth);
    }

    /**
     * Calculate the scroll x for auto open or close
     * Half of the delete button width is the change limit
     */
    public void changeScrollx() {
        if (getScrollX() >= (mScrollWidth / 2)) {
            this.smoothScrollTo(mScrollWidth, 0);
            mOpen = true;
            mOnSlidingClickListener.onMenuIsOpen(this);
        } else {
            this.smoothScrollTo(0, 0);
            mOpen = false;
        }
    }

    /**
     * Open menu to show delete button
     */
    public void openMenu() {
        if (mOpen) {
            return;
        }
        this.smoothScrollTo(mScrollWidth, 0);
        mOpen = true;
        mOnSlidingClickListener.onMenuIsOpen(this);
    }

    /**
     * Close menu to hide delete button
     */
    public void closeMenu() {
        if (!mOpen) {
            return;
        }
        this.smoothScrollTo(0, 0);
        mOpen = false;
    }

    /**
     * Set listener of sliding delete view
     * @param listener
     */
    public void setSlidingStateChangeListener(OnSlidingStateChangeListener listener) {
        mOnSlidingClickListener = listener;
    }

    /**
     * On sliding button open or close listener
     */
    public interface OnSlidingStateChangeListener<T extends SlidingDeleteView> {
        void onMenuIsOpen(View view);
        void onDownOrMove(T slidingView);
    }

}
