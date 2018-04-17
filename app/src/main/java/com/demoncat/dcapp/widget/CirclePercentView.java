package com.demoncat.dcapp.widget;
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
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.demoncat.dcapp.R;
import com.demoncat.dcapp.common.Constants;
import com.demoncat.dcapp.utils.CommonUtils;

/**
 * @Class: FakeStatusBarView
 * @Description: Circle percent view
 * @Author: hubohua
 * @CreateDate: 2018/4/17
 */
public class CirclePercentView extends View {

    // circle radius
    private float mRadius;
    // circle stripe width
    private float mStripeWidth;
    // view height and width
    private int mHeight;
    private int mWidth;

    // circle progress percent
    private int mCurPercent;

    // circle percent
    private int mPercent;
    // center coordinate x, y
    private float x;
    private float y;

    // small circle color
    private int mSmallColor;
    // big circle color
    private int mBigColor;

    // center text size
    private float mCenterTextSize;
    // bitmap of picture
    private Bitmap mBitmap;

    private String mContent = ""; // text content in view
    private Paint mBigCirclePaint = new Paint();
    private Paint mSectorPaint = new Paint();
    private RectF mSectorRect = new RectF();
    private Paint mTextPaint = new Paint();
    private Paint mBitmapPaint = new Paint();
    private float mBitmapWidth;
    private float mBitmapHeight;
    // bitmap drawing rect
    private Rect mSrcRect = new Rect();
    private Rect mDestRect = new Rect();

    private static final int DEFAULT_RADIUS = 100; // dp
    private static final int DEFAULT_STRIPE_WIDTH = 30; // dp
    private static final int DEFAULT_COLOR_BIG = 0xff6950a1;
    private static final int DEFAULT_COLOR_SMALL = 0xffafb4db;
    private static final int DEFAULT_SIZE_TEXT = 20; // sp

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0);
        mRadius = a.getDimensionPixelSize(R.styleable.CirclePercentView_radius,
                CommonUtils.dip2px(context, DEFAULT_RADIUS));
        mStripeWidth = a.getDimension(R.styleable.CirclePercentView_stripeWidth,
                CommonUtils.dip2px(context, DEFAULT_STRIPE_WIDTH));
        mCurPercent = a.getInteger(R.styleable.CirclePercentView_percent, 0);
        mSmallColor = a.getColor(R.styleable.CirclePercentView_smallColor, DEFAULT_COLOR_SMALL);
        mBigColor = a.getColor(R.styleable.CirclePercentView_bigColor, DEFAULT_COLOR_BIG);
        mCenterTextSize = a.getDimensionPixelSize(R.styleable.CirclePercentView_centerTextSize,
                CommonUtils.sp2px(context, DEFAULT_SIZE_TEXT));
        a.recycle();
        mBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.analysis_medal)).getBitmap();
        // [Customization] Set stripe width to 1/5 of the radius
        float mDefaultStripeScale = 5.0f;
        mStripeWidth = mRadius / mDefaultStripeScale;
        float mDefaultTextScale = 50 / 14.0f;
        mCenterTextSize = mRadius / mDefaultTextScale;
        Log.d(Constants.TAG_DEMONCAT,
                "init stripe width: " + mStripeWidth +
                        ", text size: " + mCenterTextSize);
        // init paint
        mBigCirclePaint.setAntiAlias(true);
        mBigCirclePaint.setColor(mBigColor);
        mBigCirclePaint.setStrokeWidth(mStripeWidth);
        mBigCirclePaint.setStyle(Paint.Style.STROKE);
        mSectorPaint.setColor(mSmallColor);
        mSectorPaint.setAntiAlias(true);
        mSectorPaint.setStrokeWidth(mStripeWidth);
        mSectorPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(mCenterTextSize);
        mTextPaint.setColor(Color.BLACK); // default color
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setFilterBitmap(true);
        mBitmapPaint.setDither(true);
        mSrcRect.set(0, 0, mBitmap.getWidth(), mBitmap.getHeight()); // get whole source bitmap
        // customization of image scale
        float mDefaultImageScale = 3.0f;
        mBitmapHeight = mRadius * 2.0f / mDefaultImageScale;
        mBitmapWidth = mBitmapHeight * mBitmap.getWidth() / (float) mBitmap.getHeight();
    }

    public void setContent(String content){
        this.mContent = content;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // force
        mWidth = (int) (mRadius * 2);
        mHeight = (int) (mRadius * 2);
        x = mRadius;
        y = mRadius;
        Log.d(Constants.TAG_DEMONCAT, "onMeasure init width: " + mWidth + ", height: "
                + mHeight + ", mRadius: " + mRadius + ", x: " + x + ", y: " + y);
        setMeasuredDimension(mWidth, mHeight);
        mSectorRect.set(mStripeWidth / 2, mStripeWidth / 2,
                mWidth - mStripeWidth / 2, mHeight - mStripeWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int endAngle = (int) (mCurPercent * 3.6);
        // Draw big circle
        canvas.drawCircle(x, y, mRadius - mStripeWidth / 2, mBigCirclePaint);
        // Draw angle circle
        canvas.drawArc(mSectorRect, 270, endAngle, false, mSectorPaint);
        // Calcute text height
        mContent = mCurPercent + "%"; // show percent text
        float textLength = mTextPaint.measureText(mContent);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = (float) Math.ceil(fm.descent - fm.ascent);
        // calculate bitmap drawing rectangle
        mDestRect.set((int) (mRadius - mBitmapWidth / 2), (int) (mRadius - mBitmapHeight / 2 - textHeight / 2),
                (int) (mRadius + mBitmapWidth / 2), (int) (mRadius + mBitmapHeight / 2 - textHeight / 2));
        canvas.drawBitmap(mBitmap, mSrcRect, mDestRect, mBitmapPaint);
        // Draw text
        canvas.drawText(mContent, x - textLength / 2,
                (int) (mRadius + mBitmapHeight / 2 + textHeight / 2), mTextPaint);
    }

    /**
     * Set percent from outside
     * @param percent
     */
    public void setPercent(int percent) {
        if (percent > 100) {
            throw new IllegalArgumentException("percent must less than 100!");
        }
        // setCurPercent(percent);
        setPercentUsingValueAnimator(percent);
    }

    // Set percent using animator of value
    private void setPercentUsingValueAnimator(int percent) {
        final ValueAnimator animator = ValueAnimator.ofInt(mCurPercent, percent);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int perc = (int) animation.getAnimatedValue();
                if (perc > 100) {
                    animator.cancel();
                    return;
                }
                mCurPercent = perc;
                CirclePercentView.this.postInvalidate();
            }
        });
        animator.start();
    }

    // Draw percent animation
    private void setCurPercent(int percent) {
        mPercent = percent;

        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 1;
                for (int i = 0; i < mPercent; i++) {
                    if (i % 20 == 0) {
                        sleepTime += 2;
                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mCurPercent = i;
                    CirclePercentView.this.postInvalidate();
                }
            }
        }).start();

    }


}
