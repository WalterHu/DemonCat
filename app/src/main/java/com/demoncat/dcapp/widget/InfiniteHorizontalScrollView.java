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
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

/**
 * @Class: InfiniteHorizontalScrollView
 * @Description: Horizontal scroll view which could scroll infinitely.
 *               Content view should be 10 times of screen width.
 * @Author: hubohua
 * @CreateDate: 2018/4/23
 */
public class InfiniteHorizontalScrollView extends HorizontalScrollView {
    private static final String TAG = InfiniteHorizontalScrollView.class.getSimpleName();

    // Has callback a reach left/right
    // forbidden the duplicate the action callback
    private boolean mLastReachLeft;
    private boolean mLastReachRight;

    private int mLastScrollX = 0;
    private int mCurrScrollX = 0; // default the first picture
    private int mCheckedScrollX = 0;
    private OnScrollChangeListener mListener;

    private Handler mCheckStateHandler;
    private int mCheckSameCount; // The same value happened count

    private boolean mScrollEnable = true;

    /**
     * Current scroll type for user interaction
     */
    public enum ScrollType {
        IDLE,
        TOUCH_SCROLL,
        FLING
    }

    private int mLastAction; // last motion event
    private ScrollType mScrollType = ScrollType.IDLE; // default scroll type
    private int mScreenWidth; // current screen width. Here we put screen with * 3 as the screen per size.
    private float mLastEventX; // last motion event on Axis X
    private int mMoveDirection = -1; // whether moves to left direction. Should be (MOVE_ACTION_LEFT, MOVE_ACTION_RIGHT)
    private static final int MOVE_ACTION_LEFT = 0;
    private static final int MOVE_ACTION_RIGHT = 1;

    // Runnable to check the position to
    // decide what scroll type is currently.
    private Runnable mScrollCheckRunnable = new Runnable() {
        @Override
        public void run() {
            mCheckStateHandler.removeCallbacks(this);
            ScrollType lastType = mScrollType;
            int currScrollX = getScrollX();
            Log.d(TAG, "mScrollCheckRunnable scrollX: " + currScrollX +
                    ", last scrollX: " + mCheckedScrollX);
            int mScrollDelayed = 50;
            if (currScrollX == mCheckedScrollX) {
                // Not scrolling
                mCheckSameCount ++;
                mScrollType = ScrollType.IDLE;
                if (mCheckSameCount >= 3 && lastType == mScrollType) {
                    mCheckSameCount = 0; // reset
                    if (mListener != null) {
                        mListener.onScrollStateChanged(mScrollType);
                    }
                } else {
                    // still to check idle state
                    mCheckedScrollX = currScrollX;
                    mCheckStateHandler.postDelayed(this, mScrollDelayed);
                }
            } else {
                // Still scrolling
                mScrollType = ScrollType.FLING;
                if (mListener != null) {
                    mListener.onScrollStateChanged(mScrollType);
                }
                mCheckedScrollX = currScrollX;
                mCheckStateHandler.postDelayed(this, mScrollDelayed);
            }
        }
    };

    private Handler mHandler = new Handler();

    public InfiniteHorizontalScrollView(Context context) {
        super(context);
        initView(context);
    }

    public InfiniteHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public InfiniteHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        HandlerThread mCheckStateThread = new HandlerThread(TAG);
        mCheckStateThread.start();
        mCheckStateHandler = new Handler(mCheckStateThread.getLooper());
        WindowManager wm
                = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
    }

    /**
     * On scroll change listener.
     * Callback interface for user.
     */
    public interface OnScrollChangeListener {
        void onScrollChanged(int scrollX);
        void onScrollStateChanged(ScrollType type);
        void onVehicleClick();
    }

    /**
     * Register for scroll change listener
     * @param listener
     */
    public void registerScrollChangeListener(OnScrollChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * Set scroll enable from outside
     * @param enable
     */
    public void setScrollEnable(boolean enable) {
        mScrollEnable = enable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mScrollEnable) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastEventX = ev.getX();
                mMoveDirection = -1;
                // resetScroll();
                break;
            case MotionEvent.ACTION_MOVE:
                resetMoveScroll(ev.getX(), mLastEventX);
                mLastEventX = ev.getX();
                mScrollType = ScrollType.TOUCH_SCROLL;
                if (mListener != null) {
                    mListener.onScrollStateChanged(mScrollType);
                }
                mCheckStateHandler.removeCallbacks(mScrollCheckRunnable);
                break;
            case MotionEvent.ACTION_UP:
                if (mLastAction == MotionEvent.ACTION_DOWN) {
                    // single click
                    if (mListener != null) {
                        mListener.onVehicleClick();
                    }
                } else {
                    // if up action, delay to check the scroll state change
                    mCheckStateHandler.post(mScrollCheckRunnable);
                }
                break;
        }
        mLastAction = ev.getAction();
        return super.onTouchEvent(ev);
    }

    /**
     * Reset scroll position
     */
    public void resetScroll() {
        scrollTo(mScreenWidth * 3, 0);
    }

    // reset move action scroll position to new X
    // handle when user move the finger to scroll the content
    private void resetMoveScroll(float newX, float oldX) {
        if (newX == oldX) {
            return;
        }
        int moveDirection = (newX - oldX > 0) ? MOVE_ACTION_LEFT : MOVE_ACTION_RIGHT;
        Log.d(TAG, "resetMoveScroll moveDirection: " + moveDirection + ", mMoveDirection: " + mMoveDirection);
        if (mMoveDirection == moveDirection) {
            return;
        }
        // when scroll to left or right
        // change the scroll position to right or left to avoid reach the edge of each side.
        mMoveDirection = moveDirection;
        if (mMoveDirection == MOVE_ACTION_LEFT) {
            // silding to left
            final int scrollXLeft = (getScrollX() % (mScreenWidth * 3) + (mScreenWidth * 3) * 2);
            Log.d(TAG, "resetMoveScroll moveDirection MOVE_ACTION_LEFT scrollXLeft: " + scrollXLeft +
                    ", getScrollX(): " + getScrollX());
            scrollTo(scrollXLeft, 0);
        } else if (mMoveDirection == MOVE_ACTION_RIGHT) {
            // sliding to right
            final int scrollXRight = (getScrollX() % (mScreenWidth * 3));
            Log.d(TAG, "resetMoveScroll moveDirection MOVE_ACTION_RIGHT scrollXRight: " + scrollXRight +
                    ", getScrollX(): " + getScrollX());
            scrollTo(getScrollX() % (mScreenWidth * 3), 0); // scroll part
        }
    }

    // handle when user fling the content of scroll view
    // when scroll to the left of right edge, it would scroll to
    // the first or end screen.
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        // should handle the overall scroll part
        int measureWidth = getMeasuredWidth();
        int childMeasureWidth = getChildAt(0).getMeasuredWidth();
        int scrollMax = childMeasureWidth - measureWidth;
        boolean byForceScroll = (scrollX - mLastScrollX) < 0;
        mLastScrollX = scrollX;
        if (scrollMax == scrollX && clampedX) {
            Log.d(TAG, "onOverScrolled on X axis right.");
            scrollToStart();
        } else if (!byForceScroll && scrollX == 0 && clampedX) {
            Log.d(TAG, "onOverScrolled on X axis left.");
            scrollToEnd(scrollMax);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int measureWidth = getMeasuredWidth();
        int childMeasureWidth = getChildAt(0).getMeasuredWidth();
        int scrollMax = childMeasureWidth - measureWidth;

        if (mCurrScrollX == l) {
            // The same scroll x avoid duplicated upload
            return;
        }
        if (getScrollX() == 0) {
            if (!mLastReachLeft) {
                Log.d(TAG, "scroll to left.");
                mCurrScrollX = 0;
                mLastReachLeft = true;
            }
            mLastReachRight = false;
        } else if (getScrollX() == scrollMax) {
            if (!mLastReachRight) {
                Log.d(TAG, "scroll to right.");
                mCurrScrollX = scrollMax;
                mLastReachRight = true;
            }
            mLastReachLeft = false;
        } else {
            Log.d(TAG, "scroll in middle.");
            mCurrScrollX = getScrollX();
            mLastReachLeft = false;
            mLastReachRight = false;
        }
        if (mListener != null) {
            mListener.onScrollChanged(mCurrScrollX);
        }
    }

    @Override
    public void fling(int velocityX) {
        // set the fling of velocity on X axis
        super.fling(velocityX / 3);
    }

    /**
     * Scroll to the first page of screen per size
     */
    private void scrollToStart() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                scrollTo(0, 0); // scroll to start x
                Log.d(TAG, "scroll to start.");
            }
        });
    }

    /**
     * Scroll to the end page of screen per size
     * @param scrollX
     */
    private void scrollToEnd(final int scrollX) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                scrollTo(scrollX, 0);
                Log.d(TAG, "scroll to end.");
            }
        });
    }
}
