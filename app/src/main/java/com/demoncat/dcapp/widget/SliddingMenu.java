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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.demoncat.dcapp.R;
import com.demoncat.dcapp.utils.CommonUtils;
import com.nineoldandroids.view.ViewHelper;

/**
 * @Class: SliddingMenu
 * @Description: Left sliding menu
 * @Author: hubohua
 * @CreateDate: 2018/4/18
 */
public class SliddingMenu extends HorizontalScrollView {

    private ViewGroup mMenuViewGroup;
    private ViewGroup mContentViewGroup;

    private int mScreenWidth;
    private int mMenuRightPadding = 50;
    private int mMenuWidth;

    private boolean mIsOnce = true;
    public boolean mIsOpen;
    public boolean mSlidable = true;
    public boolean mInterceptByChild = true;

    private int mSubHeight;

    private final static float bigScale = 0.8125f;
    private final static float smallScale = 1.0f - bigScale;

    private View mInterceptView; // intercept touch event child view

    public SliddingMenu(Context context) {
        this(context, null);
    }

    public SliddingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SliddingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSubHeight = CommonUtils.dip2px(context, 12); // default size
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        TypedArray typedArray =
                context.getTheme().obtainStyledAttributes(attrs,
                        R.styleable.SliddingMenu, defStyleAttr, 0);
        int n = typedArray.length();
        for (int i = 0; i < n; i ++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SliddingMenu_rigthtPadding:
                    mMenuRightPadding = typedArray.getDimensionPixelSize(
                            attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics())
                    );
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
    }

    /**
     * Whether slide available
     * @param slidable
     */
    public void setSlidable(boolean slidable) {
        this.mSlidable = slidable;
    }

    /**
     * Set child view which could intercept the touch event
     * @param interceptByChild
     */
    public void setInterceptByChild(boolean interceptByChild) {
        this.mInterceptByChild = interceptByChild;
    }

    /**
     * Set intercept child view
     * @param view
     */
    public void setInterceptChild(View view) {
        this.mInterceptView = view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIsOnce) {
            LinearLayout mWapper = (LinearLayout) getChildAt(0);
            mMenuViewGroup = (ViewGroup) mWapper.getChildAt(0);
            mContentViewGroup = (ViewGroup) mWapper.getChildAt(1);
            mMenuWidth = mMenuViewGroup.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContentViewGroup.getLayoutParams().width = mScreenWidth;
            mIsOnce = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mSlidable){
            return false;
        }
        if (mInterceptByChild && mInterceptView != null) {
            float touchY = ev.getY();
            int top = mInterceptView.getTop();
            int height = mInterceptView.getMeasuredHeight();
            int delta = (int) (touchY - top);
            if (delta > 0 && delta < height) {
                return false;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (!mSlidable) {
            return false;
        } else {
            switch (action) {
                case MotionEvent.ACTION_UP:
                    int scrollX = getScrollX();
                    if (scrollX >= mMenuWidth / 2) {
                        smoothScrollTo(mMenuWidth, 0);
                        mIsOpen = false;
                        if (closedListener != null)
                            closedListener.onClosed();
                    } else {
                        smoothScrollTo(0, 0);
                        mIsOpen = true;
                        if (openedListener != null)
                            openedListener.onOpened();
                    }
                    return true;
            }
        }
        return super.onTouchEvent(ev);
    }

    public void openMenu() {
        if (mIsOpen)
            return;
        smoothScrollTo(0, 0);
        mIsOpen = true;
        if (openedListener != null)
            openedListener.onOpened();
    }

    public void closeMenu() {
        if (!mIsOpen)
            return;
        smoothScrollTo(mMenuWidth, 0);
        mIsOpen = false;
        if (closedListener != null)
            closedListener.onClosed();
    }

    public void toggleMenu() {
        if (mIsOpen)
            closeMenu();
        else
            openMenu();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int contentH = mContentViewGroup.getHeight();
        // float bigScale = ((float) (contentH - mSubHeight * 10)) / ((float) (contentH + mSubHeight * 2));

        float scale = l * 1.0f / mMenuWidth; // l: 1.0 ~ 0
        ViewHelper.setTranslationX(mMenuViewGroup, mMenuWidth * scale * bigScale);

        float rightScale = bigScale + smallScale * scale;
        ViewHelper.setPivotX(mContentViewGroup, 0);
        ViewHelper.setPivotY(mContentViewGroup, contentH / 2 - mSubHeight);
        ViewHelper.setScaleX(mContentViewGroup, rightScale);
        ViewHelper.setScaleY(mContentViewGroup, rightScale);

        float leftScale = 1.0f - smallScale * scale;
        ViewHelper.setScaleX(mMenuViewGroup, leftScale);
        ViewHelper.setScaleY(mMenuViewGroup, leftScale);

        float leftAlpha = 0.1f + 0.9f * (1 - scale);
        ViewHelper.setAlpha(mMenuViewGroup, leftAlpha);
    }

    public interface OnOpenedListener {
        void onOpened();
    }

    public interface OnClosedListener {
        void onClosed();
    }

    OnOpenedListener openedListener = null;
    OnClosedListener closedListener = null;

    public void setOpenedListener(OnOpenedListener openedListener) {
        this.openedListener = openedListener;
    }

    public void setClosedListener(OnClosedListener closedListener) {
        this.closedListener = closedListener;
    }
}
