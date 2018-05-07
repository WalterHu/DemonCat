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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * @Class: FrameAnimation
 * @Description: Frame animation utils
 * @Author: hubohua
 * @CreateDate: 2018/4/28
 */
public class FrameAnimation {
    private static final String TAG = "FrameAnimation";

    private ImageView mImageView;
    private Bitmap mBitmap;
    private int mCurrIndex;
    private int mDuration;

    private OnAnimationListener mListener;

    private Handler mHandler = new FrameAnimHandler();

    private static class FrameAnimHandler extends Handler {}

    private int[] mFrameRes;

    public FrameAnimation(ImageView imageView, int[] frameRes, int duration) {
        this.mImageView = imageView;
        this.mFrameRes = frameRes;
        this.mDuration = duration;
    }

    public void setAnimationListener(OnAnimationListener listener) {
        this.mListener = listener;
    }

    public void start() {
        Log.d(TAG, "start");
        mCurrIndex = -1;
        mHandler.post(new FrameAnimRunnable(this));
        if (mListener != null) {
            mListener.onAnimationStart();
        }
    }

    public void stop() {
        Log.d(TAG, "stop");
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (mListener != null) {
            mListener.onAnimationFinish();
            mListener = null;
        }
        if (mImageView != null) {
            mImageView.setVisibility(View.GONE);
            mImageView.setImageDrawable(null);
            mImageView = null;
        }
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        System.gc(); // call the gc for vm
    }

    private void playNext() {
        Log.d(TAG, "playNext mCurrIndex: " + mCurrIndex);
        mCurrIndex ++;
        if (mCurrIndex > mFrameRes.length - 1) {
            // reach the next
            mCurrIndex = -1;
            stop();
            return;
        }
        if (mImageView != null) {
            mBitmap = createBitmap(mFrameRes[mCurrIndex]);
            Log.d(TAG, "playNext mBitmap: " + mBitmap);
            mImageView.setImageBitmap(mBitmap);
            mHandler.postDelayed(new FrameAnimRunnable(this), mDuration);
        }
    }

    private Bitmap createBitmap(int res) {
        Log.d(TAG, "createBitmap mCurrIndex: "
                + mCurrIndex + ", mBitmap: " + mBitmap);
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inSampleSize = 1;
        if (mBitmap != null) {
            // reuse the bitmap
            options.inBitmap = mBitmap;
        }
        bitmap =
                BitmapFactory.decodeResource(mImageView.getResources(), res, options);
        return bitmap;
    }

    private static class FrameAnimRunnable implements Runnable {
        private FrameAnimation host;

        public FrameAnimRunnable(FrameAnimation host) {
            this.host = host;
        }

        @Override
        public void run() {
            Log.d(TAG, "FrameAnimRunnable host: " + host);
            if (host != null) {
                host.playNext();
                host = null;
            }
        }
    }

    public interface OnAnimationListener {
        void onAnimationStart();
        void onAnimationFinish();
    }
}
