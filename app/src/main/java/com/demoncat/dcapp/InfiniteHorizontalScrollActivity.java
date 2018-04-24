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
import android.widget.Button;
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
    private Button mBtnAutoAngle;
    private ImageView mIvCarStatus;
    private BitmapCache mVehicleCache;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVehicleCache = new BitmapCache(getApplicationContext());
        setContentView(R.layout.activity_infinite_scroll);

        // init scroll layout
        mScrollView = (InfiniteHorizontalScrollView) findViewById(R.id.hori_scroll_view);
        mScrollView.registerScrollChangeListener(this);
        mIvCarStatus = (ImageView) findViewById(R.id.img_vehicle);
        // init reset button
        mBtnAutoAngle = findViewById(R.id.btn_reset);
        mBtnAutoAngle.setText(mScrollView.isAutoFixAngle() ? "随意角度" : "修正角度");
        mBtnAutoAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScrollView != null) {
                    // mScrollView.resetScroll();
                    mScrollView.setAutoFixAngle(!mScrollView.isAutoFixAngle());
                    mBtnAutoAngle.setText(mScrollView.isAutoFixAngle() ? "随意角度" : "修正角度");
                }
            }
        });
    }

    @Override
    public void onScrollChanged(final int index) {
        if (index >= 0) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getImageResource(index);
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
        // 52 image count and 360 angles
        index = (int) Math.round((index / 360.0f) * 52.0f + 0.5);
        if (index <= 0) {
            index = 1;
        } else if (index > 52) { // max image number
            index = 52;
        }
        return index;
    }

    @Override
    public void onScrollStateChanged(InfiniteHorizontalScrollView.ScrollType type) {
        Log.d(TAG, "onScrollStateChanged type: " + type);
    }

    @Override
    public void onVehicleClick() {
    }
}
