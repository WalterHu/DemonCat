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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.demoncat.dcapp.common.Constants;
import com.demoncat.dcapp.utils.BitmapCache;
import com.demoncat.dcapp.utils.CommonUtils;
import com.demoncat.dcapp.widget.InfiniteHorizontalScrollView;

/**
 * @Class: InfiniteHorizontalScrollActivity
 * @Description: Infinite scroll horizontally
 * @Author: hubohua
 * @CreateDate: 2018/4/23
 */
public class InfiniteHorizontalScrollActivity extends Activity implements
        InfiniteHorizontalScrollView.OnScrollChangeListener {
    private static final String TAG = InfiniteHorizontalScrollActivity.class.getSimpleName();

    private InfiniteHorizontalScrollView mScrollView;
    private View mScrollContent;
    private int mSwitchStep = 1; // default switch step for one vehicle image switch to next
    private int mScrollPer; // vehicle scrolling of 45 angles corresponds to screen width * 3
    private ImageView mIvCarStatus;
    private int srcNum; // current scroll angle for 360.
    private BitmapCache mVehicleCache;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVehicleCache = new BitmapCache(getApplicationContext());
        setContentView(R.layout.activity_infinite_scroll);

        // init scroll layout
        mScrollView = (InfiniteHorizontalScrollView) findViewById(R.id.hori_scroll_view);
        mScrollView.setSmoothScrollingEnabled(true);
        mScrollView.registerScrollChangeListener(this);
        mScrollContent = findViewById(R.id.scroll_content);
        // init scroll content
        mScrollContent = findViewById(R.id.scroll_content);
        WindowManager wm
                = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = mScrollContent.getLayoutParams();
        if (params == null) {
            params
                    = new ViewGroup.LayoutParams(screenWidth * 10, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        // set the width of content view to 10 times of screen width
        params.width = screenWidth * 10;
        mScrollContent.setLayoutParams(params);
        // init params
        int maxNum = 360; // max number of angle
        // switch image step in pixels for 360 angles
        // per scroll width screen width * 3
        mSwitchStep = (int) (screenWidth * 3.0 / maxNum + 0.5);
        mScrollPer = screenWidth * 3 / 8;
        mIvCarStatus = (ImageView) findViewById(R.id.img_vehicle);
        srcNum = 0;
        // init the vehicle scroll state to first per screen size position
        if (mScrollView != null) {
            mScrollView.resetScroll();
        }
    }

    @Override
    public void onScrollChanged(final int scrollX) {
        if (scrollX >= 0) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    int index = (int) (scrollX  * 1.0 / mSwitchStep + 0.5);
                    Log.d(TAG, "onScrollChangeListener scrollX: " + scrollX
                            + ", mSwitchStep: " + mSwitchStep + ", index: " + index);
                    index += 90; // add 90 for image name (vehicle image state)
                    if (index >= 360) {
                        index = (index % 360);
                    }
                    Log.d(TAG, "onScrollChangeListener index: " + index);
                    if (index >= 0 && srcNum != index) {
                        srcNum = index;
                        getImageResource(srcNum);
                    }
                }
            });
        }
    }

    /**
     * Get image resource id
     * @param index
     * @return
     */
    private void getImageResource(int index) {
        Log.d(TAG, "getImageResource index: " + index);
        if (index >= 0 && index < 360) {
            index = splitIndex(index + 1);
            String num = String.valueOf(index);
            Log.d(TAG, "getImageResource num: " + num);
            Bitmap bitmap = mVehicleCache.getBitmapFromMemCache(num);
            try {
                if (bitmap == null || bitmap.isRecycled()) {
                    String drawableId = "p" + num;
                    // int resId = getResources().getIdentifier(drawableId,
                    //         "drawable", getPackageName());
                    int resId = CommonUtils.getResId(drawableId, R.drawable.class);
                    Log.d(TAG, "getImageResource resId: " + resId);
                    bitmap = BitmapFactory.decodeResource(getResources(), resId);
                    mVehicleCache.addBitmapToMemoryCache(num,
                            bitmap);
                }
                mIvCarStatus.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
            }
        }
    }

    // calculate the index from 360 angle to current vehicle image count
    private int splitIndex(int index) {
//        if (index % 3 == 1) {
//            // 10
//            index = index;
//        } else if (index % 3 == 2) {
//            // 11
//            index = index - 1;
//        } else if (index % 3 == 0) {
//            // 12
//            index = index - 2;
//        }
//        index = (index + 2) / 3;
//        return index;
        // 52 image count and 360 angles
        index = (int) Math.round((index / 360.0f) * 52.0f + 0.5);
        return index;
    }

    @Override
    public void onScrollStateChanged(InfiniteHorizontalScrollView.ScrollType type) {
        Log.d(TAG, "onScrollStateChanged type: " + type);
        if (type == InfiniteHorizontalScrollView.ScrollType.IDLE) {
            // When scrolling stops, we find the nearest angle to scroll to.
            // The angle is integer times of 45 angle.
            final int scrollX = mScrollView.getScrollX();
            autoScrollTo(scrollX);
        }
    }

    /**
     * Auto scroll to correspond axis x
     * @param scrollX
     */
    private void autoScrollTo(int scrollX) {
        int nextIndex = scrollX / mScrollPer;
        int t = scrollX % mScrollPer;
        if (t > mScrollPer / 2) {
            nextIndex ++;
        }
        // Scroll to index
        int targetScrollX = nextIndex * mScrollPer;
        mScrollView.smoothScrollTo(targetScrollX, 0);
    }

    @Override
    public void onVehicleClick() {

    }
}
